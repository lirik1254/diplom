//package backend.academy.diplom.services;
//
//import backend.academy.diplom.DTO.collection.CollectionDTO;
//import backend.academy.diplom.entities.Collection;
//import backend.academy.diplom.entities.user.User;
//import backend.academy.diplom.repositories.CollectionRepository;
//import backend.academy.diplom.utils.JwtUtils;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class CollectionService {
//    private final CollectionRepository collectionRepository;
//    private final JwtUtils jwtUtils;
//
//    @Transactional
//    public void addCollection(String authHeader, String name, String description) {
//        User user = jwtUtils.getUserByAuthHeader(authHeader);
//        collectionRepository.addCollection(user.getId(), name, description);
//    }
//
//    @Transactional
//    public void deleteCollection(String authHeader, String name) {
//        User user = jwtUtils.getUserByAuthHeader(authHeader);
//        collectionRepository.deleteCollection(user.getId(), name);
//    }
//
//    @Transactional
//    public void editCollection(String authHeader, String oldName, String newName,
//                               String description) {
//        User user = jwtUtils.getUserByAuthHeader(authHeader);
//        collectionRepository.editCollection(user.getId(), oldName, newName, description);
//    }
//
//    public CollectionDTO getCollection(String authHeader, String name) {
//        User user = jwtUtils.getUserByAuthHeader(authHeader);
//        Collection collection = collectionRepository.getCollection(user.getId(), name);
//        return new CollectionDTO(collection.getName(), collection.getDescription());
//    }
//}
