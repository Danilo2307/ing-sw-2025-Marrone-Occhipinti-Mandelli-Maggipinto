package it.polimi.ingsw.psp23.protocol.response;


import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * The StringResponse class represents an event used to send textual information
 * from the server to the client. This information may include confirmations,
 * operation outcomes, or any other general message.
 *
 * This class implements the Event interface and follows the Visitor design pattern
 * to allow interaction with an EventVisitor for specific processing.
 *
 * Additionally, the toString method returns the contained message as its string
 * representation.
 *
 * Note: Some StringResponse events are not intended for GUI message display, such as
 * responses that involve non-visual entity representation like getVisibleDeck. These
 * use cases may need to be categorized into new event types to reflect their purpose.
 *
 * @param message The textual message associated with this event.
 */
public record StringResponse(String message) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showMessage(message);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI) {
        return eventVisitor.visitForStringResponse(this, viewAPI);
    }

    @Override
    public String toString() {
        return message;
    }

}
