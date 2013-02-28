package org.iplantc.workflow.marshaller;

import org.iplantc.workflow.marshaler.NotificationSetUnmarshaller;
import org.iplantc.workflow.template.notifications.NotificationSet;
import org.json.JSONObject;

import junit.framework.TestCase;

public class TestNotificationSetUnmarshaller extends TestCase {


	public void testUnmarshallNotificationSet() throws Exception{


		String json_string =  "{\"id\":\"n12b7b4bc1c633352e31302e3231da4bdc3184871da9\",\"name\":\" \",\"template_id\":\"t12af368916d33352e31302e3231d01170012afe3c9\",\"wizardNotifications\" : [{\"sender\": \"skipBarcodeSplitter\", \"type\": \"disableOnSelection\", \"receivers\": [\"barcodeEntryOption\", \"numberOfAllowedMismatches\"]},"
            + "{\"sender\": \"skipClipper\", \"type\": \"disableOnSelection\", \"receivers\": [\"adapterEntryOption\", \"minSequenceLengthAfterClipping\", \"discardSequencesWithUnknownBases\", \"outputOption\"]}, "
            + "{\"sender\": \"skipTrimmer\", \"type\": \"disableOnSelection\", \"receivers\": [\"firstBaseToKeep\", \"lastBaseToKeep\"]}, "
            + "{\"sender\": \"skipQualityFilter\", \"type\": \"disableOnSelection\", \"receivers\": [\"qualityCutOff\", \"percentBasesAboveCutoff\"]}"
            + "]}";

		JSONObject json = new JSONObject(json_string);

		NotificationSetUnmarshaller unmarshaller = new NotificationSetUnmarshaller();

		NotificationSet set = unmarshaller.unmarshallNotificationSet(json);


		assertEquals(4,set.getNotifications().size());


	}

}
