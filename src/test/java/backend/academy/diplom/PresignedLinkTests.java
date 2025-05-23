package backend.academy.diplom;

import backend.academy.diplom.DTO.PutLinkDTO;
import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.services.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URL;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PresignedLinkTests extends TestBase {

    @MockitoSpyBean
    FileService fileService;

    @MockitoBean
    S3Presigner presigner;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void getPutLink() throws Exception {
        String accessToken = getDefaultRegWithAccess();

        PresignedPutObjectRequest presignedMock = mock(PresignedPutObjectRequest.class);
        URL fakeUrl = new URL("https://example.com/my-presigned-url");
        when(presignedMock.url()).thenReturn(fakeUrl);

        when(presigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .thenReturn(presignedMock);

        mockMvc.perform(get("/api/put-link")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("filePath", "someAndroidFile.txt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.link").value("https://example.com/my-presigned-url"));
    }

    @Test
    public void getGetLinkDiploma() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        PresignedGetObjectRequest presignedMock = mock(PresignedGetObjectRequest.class);
        URL fakeUrl = new URL("https://example.com/my-presigned-url");
        when(presignedMock.url()).thenReturn(fakeUrl);

        when(presigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenReturn(presignedMock);


        mockMvc.perform(get("/api/get-link")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("type", "diploma"))
                .andExpect(status().isOk())
                .andExpect(content().string("https://example.com/my-presigned-url"));
    }

    @Test
    public void getPresignedLink() throws Exception {
        PresignedGetObjectRequest presignedMock = mock(PresignedGetObjectRequest.class);
        URL fakeUrl = new URL("https://example.com/my-presigned-url");
        when(presignedMock.url()).thenReturn(fakeUrl);

        when(presigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenReturn(presignedMock);

        String link = fileService.getPresignedLinkIfExist("something");
        assertEquals("https://example.com/my-presigned-url", link);
    }

    @Test
    public void getGetLinkResume() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        PresignedGetObjectRequest presignedMock = mock(PresignedGetObjectRequest.class);
        URL fakeUrl = new URL("https://example.com/my-presigned-url");
        when(presignedMock.url()).thenReturn(fakeUrl);

        when(presigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenReturn(presignedMock);


        mockMvc.perform(get("/api/get-link")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("type", "resume"))
                .andExpect(status().isOk())
                .andExpect(content().string("https://example.com/my-presigned-url"));
    }

    @Test
    public void getGetLinkPhoto() throws Exception {
        String accessToken = getDefaultRegWithAccess();
        PresignedGetObjectRequest presignedMock = mock(PresignedGetObjectRequest.class);
        URL fakeUrl = new URL("https://example.com/my-presigned-url");
        when(presignedMock.url()).thenReturn(fakeUrl);

        when(presigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenReturn(presignedMock);


        mockMvc.perform(get("/api/get-link")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("type", "photo"))
                .andExpect(status().isOk())
                .andExpect(content().string("https://example.com/my-presigned-url"));
    }

    @Test
    public void putLinkSaveTestDiploma() throws Exception {
        String accessToken = getDefaultRegWithAccess();

        mockMvc.perform(post("/api/put-link")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("type", "diploma")
                        .param("filePath", "http://filePathThatWasDownload"))
                .andExpect(status().isOk());

        User user = userRepository.findById(1L).getFirst();
        assertEquals("http://filePathThatWasDownload", user.getDiplomaPath());
    }

    @Test
    public void putLinkSaveTestResume() throws Exception {
        String accessToken = getDefaultRegWithAccess();

        mockMvc.perform(post("/api/put-link")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("type", "resume")
                        .param("filePath", "http://filePathThatWasDownload"))
                .andExpect(status().isOk());

        User user = userRepository.findById(1L).getFirst();
        assertEquals("http://filePathThatWasDownload", user.getResumePath());
    }

    @Test
    public void putLinkSaveTestPhoto() throws Exception {
        String accessToken = getDefaultRegWithAccess();

        mockMvc.perform(post("/api/put-link")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .param("type", "photo")
                        .param("filePath", "http://filePathThatWasDownload"))
                .andExpect(status().isOk());

        User user = userRepository.findById(1L).getFirst();
        assertEquals("http://filePathThatWasDownload", user.getPhotoPath());
    }
}
