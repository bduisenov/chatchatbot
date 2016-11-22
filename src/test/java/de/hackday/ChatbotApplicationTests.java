package de.hackday;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/*
@RunWith(SpringRunner.class)
@SpringBootTest
*/
public class ChatbotApplicationTests {

	private ChatbotApplication app = new ChatbotApplication();

	@Test
	public void contextLoads() {
	}

	@Test
	public void process() {
		/*TelegramResponse response = new TelegramResponse(true,
				Collections.singletonList(new TelegramResponse.Payload(100, new TelegramResponse.Message())));

		long result = app.processResponse(response);

		assertEquals(101, result);*/
	}

}
