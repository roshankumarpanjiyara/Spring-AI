package com.example.springAI.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OllamaController {

	private ChatClient chatClient;
	private ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

	// for multi model can be used
	private OllamaController(OllamaChatModel chatModel) {
		this.chatClient = ChatClient.create(chatModel);
	}

//	@GetMapping("/api/{message}")
	public ResponseEntity<String> getAnswer(@PathVariable String message) {
//		String response = chatClient.prompt(message)
//									.call().content();

		ChatResponse chatResponse = chatClient.prompt(message).call().chatResponse();

		System.out.println(chatResponse.getMetadata().getModel());

		String response = chatResponse.getResult().getOutput().getText();

		return ResponseEntity.ok(response);
	}
}
