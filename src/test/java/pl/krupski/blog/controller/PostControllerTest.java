package pl.krupski.blog.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String TEXT = "test_text";
    private static final String TEXT2 = "test_text2";

    @AfterEach
    public void cleanUp() throws Exception {
        String posts = this.mockMvc.perform(get("/post"))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONArray jsonArray = new JSONArray(posts);
        int bound = jsonArray.length();
        for (int i = 0; i < bound; i++) {
            this.mockMvc.perform(delete("/post")
                    .content(((JSONObject) jsonArray.get(i)).optString("postId")));
        }
    }

    @Test
    public void shouldReturnTextFromPost() throws Exception {
        this.mockMvc.perform(post("/post").content(TEXT))
                .andExpect(status().isCreated());

        this.mockMvc.perform(get("/post"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].text", is(TEXT)));
    }

    @Test
    public void shouldDeletePost() throws Exception {
        this.mockMvc.perform(post("/post").content(TEXT))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/post").content(TEXT2))
                .andExpect(status().isCreated());

        MvcResult result = this.mockMvc.perform(get("/post"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].text", is(TEXT)))
                .andExpect(jsonPath("$[1].text", is(TEXT2)))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        String id = getFieldFromPosts(content, 0, "postId");

        this.mockMvc.perform(delete("/post").content(id))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/post"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].text", is(TEXT2)))
                .andReturn();
    }

    @Test
    public void shouldFindExcitingPost() throws Exception {
        this.mockMvc.perform(post("/post").content(TEXT))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/post").content(TEXT2))
                .andExpect(status().isCreated());

        MvcResult result = this.mockMvc.perform(get("/post"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].text", is(TEXT)))
                .andExpect(jsonPath("$[1].text", is(TEXT2)))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        String id = getFieldFromPosts(content, 1, "postId");

        this.mockMvc.perform(get("/findPost").content(id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.text", is(TEXT2)));
    }

    @Test
    public void shouldNotFindUnexcitingPost() throws Exception {
        this.mockMvc.perform(post("/post").content(TEXT))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldEditPost() throws Exception {
        this.mockMvc.perform(post("/post").content(TEXT))
                .andExpect(status().isCreated());

        MvcResult result = this.mockMvc.perform(get("/post"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].text", is(TEXT)))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        String id = getFieldFromPosts(content, 0, "postId");

        this.mockMvc.perform(put("/post").content(TEXT2).param("id", id))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/post"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].text", is(TEXT2)))
                .andReturn();
    }

    private String getFieldFromPosts(String content, int index, String field) throws JSONException {
        JSONArray jsonArray = new JSONArray(content);
        List<String> list = new ArrayList<>();
        int bound = jsonArray.length();
        for (int i = 0; i < bound; i++) {
            String s = ((JSONObject) jsonArray.get(i)).optString(field);
            list.add(s);
        }
        return list.get(index);
    }

}