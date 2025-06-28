package com.example.springAI.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImgGenController {

	private ChatClient chatClient;

	private OpenAiImageModel openAiImageModel;

	private ImgGenController(OpenAiImageModel openAiImageModel, OpenAiChatModel chatModel) {
		this.openAiImageModel = openAiImageModel;
		this.chatClient = ChatClient.create(chatModel);
	}
	
	@GetMapping("image/{query}")
	public String genImage(@PathVariable String query) {
		ImagePrompt prompt = new ImagePrompt(query, OpenAiImageOptions.builder()
				.quality("hd")
				.height(1024)
				.width(1024)
				.style("natural")
				.build());
		ImageResponse response = openAiImageModel.call(prompt);
		return response.getResult().getOutput().getUrl();
	}
	
	
	@PostMapping("image/describe")
	public String descImage(@RequestParam String query, @RequestParam MultipartFile file) {
		System.out.println(file.getOriginalFilename());
		return chatClient.prompt()
						 .user(us -> us.text(query)
						 .media(MimeTypeUtils.IMAGE_JPEG, file.getResource()))
						 .call()
						 .content();
	}
}
