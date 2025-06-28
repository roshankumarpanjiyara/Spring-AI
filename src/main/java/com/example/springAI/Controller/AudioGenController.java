package com.example.springAI.Controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.Voice;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AudioGenController {
	
	
	private OpenAiAudioTranscriptionModel audioModel;
	
	private OpenAiAudioSpeechModel speechModel;
	
	private AudioGenController(OpenAiAudioTranscriptionModel audioModel, OpenAiAudioSpeechModel speechModel) {
		this.audioModel = audioModel;
		this.speechModel = speechModel;
	}
	
	@PostMapping("api/speechToText")
	public String speechToText(@RequestParam MultipartFile file) {
		OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
//				.language("es")//spanish
				.responseFormat(TranscriptResponseFormat.SRT)
				.build();
		AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(file.getResource(), options);
		return audioModel.call(prompt).getResult().getOutput();
	}
	
	
	@PostMapping("api/textTpSpeech")
	public byte[] textToSpeech(@RequestParam String text) {
		OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
				.speed(1.5f)
				.voice(Voice.NOVA)
				.build();
		SpeechPrompt prompt = new SpeechPrompt(text, options);
		return speechModel.call(prompt).getResult().getOutput();
	}
}
