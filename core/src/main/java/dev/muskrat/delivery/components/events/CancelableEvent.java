package dev.muskrat.delivery.components.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

public abstract class CancelableEvent extends ApplicationEvent {

    @Getter
    @Setter
    private boolean cancel;

    @Getter
    private String reason;

    public CancelableEvent(Object source) {
        super(source);
        this.cancel = false;
        reason = String.format("System error in event %s", this.getClass().getName());
    }

    public void setReason(String reason) {
        this.cancel = true;
        this.reason = reason;
    }
}
