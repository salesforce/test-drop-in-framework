package com.salesforce.selenium.support.event.internal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;

import com.salesforce.selenium.support.event.WebDriverEventListener;

/**
 * A keyboard firing events.
 */
public class EventFiringKeyboard implements Keyboard {
	private final WebDriver driver;
//	private final EnhancedWebDriverEventListener dispatcher;
	private final Keyboard keyboard;

	public EventFiringKeyboard(WebDriver driver, WebDriverEventListener dispatcher) {
		this.driver = driver;
//		this.dispatcher = dispatcher;
		this.keyboard = ((HasInputDevices) this.driver).getKeyboard();

	}

	public void sendKeys(CharSequence... keysToSend) {
		keyboard.sendKeys(keysToSend);
	}

	public void pressKey(CharSequence keyToPress) {
		keyboard.pressKey(keyToPress);
	}

	public void releaseKey(CharSequence keyToRelease) {
		keyboard.releaseKey(keyToRelease);
	}
}
