import logging

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import sqlalchemy as sa
from sqlalchemy.orm import sessionmaker
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

logging.info("чето пытаюсь")
# --- Database setup ---
# DATABASE_URL = 'postgresql+psycopg2://postgres:postgres@localhost:5432/engineeringDB' это для локального
DATABASE_URL = 'postgresql+psycopg2://postgres:postgres@db:5432/engineeringDB'
engine = sa.create_engine(DATABASE_URL)
SessionLocal = sessionmaker(bind=engine)
metadata = sa.MetaData(schema='engineers')
Base = sa.orm.declarative_base(metadata=metadata)


# --- Models ---
class ProjectSectionStamp(Base):
    __tablename__ = 'project_section_and_stamp'
    project_id = sa.Column(sa.BigInteger, primary_key=True)
    section_and_stamp_id = sa.Column(sa.BigInteger, primary_key=True)


class ProjectSoftwareSkill(Base):
    __tablename__ = 'project_software_skill'
    project_id = sa.Column(sa.BigInteger, primary_key=True)
    software_skill_id = sa.Column(sa.BigInteger, primary_key=True)


class UserSectionStamp(Base):
    __tablename__ = 'user_section_and_stamp'
    user_id = sa.Column(sa.BigInteger, primary_key=True)
    section_and_stamp_id = sa.Column(sa.BigInteger, primary_key=True)


class UserSoftwareSkill(Base):
    __tablename__ = 'user_software_skill'
    user_id = sa.Column(sa.BigInteger, primary_key=True)
    software_skill_id = sa.Column(sa.BigInteger, primary_key=True)


class Project(Base):
    __tablename__ = 'project'
    id = sa.Column(sa.BigInteger, primary_key=True)
    name = sa.Column(sa.Text)


class User(Base):
    __tablename__ = "user"
    id = sa.Column(sa.BigInteger, primary_key=True)
    name = sa.Column(sa.Text)


# --- Pydantic response schema ---
class Recommendation(BaseModel):
    projectId: int


# --- FastAPI setup ---
app = FastAPI()


@app.get('/recommend/{user_id}', response_model=list[Recommendation])
def recommend_projects(user_id: int):
    session = SessionLocal()
    try:
        print("я чето пытаюсь")
        logging.info("чето пытаюсь")
        # Load global tag lists
        all_sections = [r[0] for r in session.query(ProjectSectionStamp.section_and_stamp_id).distinct()]
        all_skills = [r[0] for r in session.query(ProjectSoftwareSkill.software_skill_id).distinct()]
        sec_index = {sec: i for i, sec in enumerate(all_sections)}
        skill_index = {skill: i for i, skill in enumerate(all_skills)}
        D = len(all_sections) + len(all_skills)

        # Build user vector
        u_vec = np.zeros(D, dtype=int)
        for (sec_id,) in session.query(UserSectionStamp.section_and_stamp_id).filter_by(user_id=user_id):
            u_vec[sec_index[sec_id]] = 1
        for (skill_id,) in session.query(UserSoftwareSkill.software_skill_id).filter_by(user_id=user_id):
            u_vec[len(all_sections) + skill_index[skill_id]] = 1

        # Build project vectors
        projects = session.query(Project).all()
        mat = []
        for proj in projects:
            p_vec = np.zeros(D, dtype=int)
            for (sec_id,) in session.query(ProjectSectionStamp.section_and_stamp_id).filter_by(project_id=proj.id):
                p_vec[sec_index[sec_id]] = 1
            for (skill_id,) in session.query(ProjectSoftwareSkill.software_skill_id).filter_by(project_id=proj.id):
                p_vec[len(all_sections) + skill_index[skill_id]] = 1
            mat.append(p_vec)
        if not mat:
            return []

        # Compute similarity
        sim = cosine_similarity([u_vec], np.vstack(mat))[0]
        ranked_idx = np.argsort(sim)[::-1]

        # Prepare result
        recommendations = []
        for idx in ranked_idx:
            recommendations.append(Recommendation(
                projectId=projects[idx].id
            ))
        return recommendations
    finally:
        session.close()


if __name__ == '__main__':
    import uvicorn

    uvicorn.run(app, host='0.0.0.0', port=8082)
