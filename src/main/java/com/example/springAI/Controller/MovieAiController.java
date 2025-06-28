package com.example.springAI.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springAI.Entity.Movie;

@RestController
public class MovieAiController {

	private ChatClient chatClient;
	private ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

	@Autowired
	private VectorStore vectorStore;

	// for multi model can be used
	private MovieAiController(OpenAiChatModel chatModel) {
		this.chatClient = ChatClient.create(chatModel);
	}

	@GetMapping("/api/movies/list")
	public List<String> getMoviesList(@RequestParam String name) {

		String temp = """
				List Top 5 movies of {name}
				{format}
				""";

		ListOutputConverter con = new ListOutputConverter(new DefaultConversionService());
		PromptTemplate promptTemplate = new PromptTemplate(temp);
		Prompt prompt = promptTemplate.create(Map.of("name", name, "format", con.getFormat()));

		List<String> response = con.convert(chatClient.prompt(prompt).call().content());
		return response;
	}

	@GetMapping("/api/movies/details")
	public Movie getMovieData(@RequestParam String name) {
		String temp = """
				Get me the best movie of {name}
				{format}
				""";

		BeanOutputConverter<Movie> beanCon = new BeanOutputConverter<>(Movie.class);
		PromptTemplate promptTemplate = new PromptTemplate(temp);
		Prompt prompt = promptTemplate.create(Map.of("name", name, "format", beanCon.getFormat()));

		Movie response = beanCon.convert(chatClient.prompt(prompt).call().content());
		return response;
	}

	@GetMapping("/api/movies/list/details")
	public List<Movie> getMovieListData(@RequestParam String name) {
		String temp = """
				List Top 5 movies of {name}
				{format}
				""";

		BeanOutputConverter<List<Movie>> beanCon = new BeanOutputConverter<>(
				new ParameterizedTypeReference<List<Movie>>() {
				});
		PromptTemplate promptTemplate = new PromptTemplate(temp);
		Prompt prompt = promptTemplate.create(Map.of("name", name, "format", beanCon.getFormat()));

		List<Movie> response = beanCon.convert(chatClient.prompt(prompt).call().content());
		return response;
	}

}
