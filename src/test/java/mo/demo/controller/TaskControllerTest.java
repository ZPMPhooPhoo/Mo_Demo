package mo.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mo.demo.model.Task;
import mo.demo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
public class TaskControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testCreateTask_Success() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");

        when(taskService.createTask(any(Task.class), any())).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testGetMyTasks_Success() throws Exception {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setTitle("Task 2");
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getMyTasks(any())).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = {"APPROVER"})
    void testGetSubmittedTasks_Success() throws Exception {
        Task task1 = new Task();
        task1.setTitle("Submitted Task 1");
        List<Task> tasks = Arrays.asList(task1);

        when(taskService.getSubmittedTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/submitted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(roles = {"APPROVER"})
    void testApproveTask_Success() throws Exception {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setStatus(Task.TaskStatus.APPROVED);

        when(taskService.approveTask(any(UUID.class), any(String.class))).thenReturn(task);

        mockMvc.perform(put("/api/tasks/{id}/approve", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @WithMockUser(roles = {"APPROVER"})
    void testRejectTask_Success() throws Exception {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setStatus(Task.TaskStatus.REJECTED);

        when(taskService.rejectTask(any(UUID.class), any(String.class), any(String.class))).thenReturn(task);

        mockMvc.perform(put("/api/tasks/{id}/reject", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testSubmitTask_Success() throws Exception {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setStatus(Task.TaskStatus.SUBMITTED);

        when(taskService.submitTask(any(UUID.class), any())).thenReturn(task);

        mockMvc.perform(put("/api/tasks/{id}/submit", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUBMITTED"));
    }

    @Test
    void testCreateTask_Unauthorized() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testApproveTask_Forbidden() throws Exception {
        mockMvc.perform(put("/api/tasks/{id}/approve", UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }
}
