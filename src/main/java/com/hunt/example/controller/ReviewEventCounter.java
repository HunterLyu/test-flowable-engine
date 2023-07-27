package com.hunt.example.controller;

import liquibase.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.eventregistry.api.EventConsumerInfo;
import org.flowable.eventregistry.api.EventRegistryEvent;
import org.flowable.eventregistry.api.EventRegistryEventConsumer;
import org.flowable.eventregistry.api.EventRegistryProcessingInfo;
import org.flowable.eventregistry.api.runtime.EventInstance;
import org.flowable.eventregistry.model.EventModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Filip Hrisafov
 */
@Service
public class ReviewEventCounter implements EventRegistryEventConsumer {

    private final AtomicLong eventCounter = new AtomicLong(0);


    public long getEventCount() {
        return eventCounter.get();
    }

    protected void eventReceived(EventInstance eventInstance) {
        String eventKey = eventInstance.getEventKey();
        if (StringUtils.isNotBlank(eventKey)) {
            eventCounter.incrementAndGet();
        }
    }

    @Override
    public EventRegistryProcessingInfo eventReceived(EventRegistryEvent event) {
        EventRegistryProcessingInfo eventRegistryProcessingInfo = new EventRegistryProcessingInfo();

        if (event.getEventObject() != null && event.getEventObject() instanceof EventInstance) {
            eventReceived((EventInstance) event.getEventObject());

            EventConsumerInfo eventInfo = new EventConsumerInfo();
            eventInfo.setEventSubscriptionId(UUID.randomUUID().toString());
            eventRegistryProcessingInfo.addEventConsumerInfo(eventInfo);
        } else {
            if (event.getEventObject() == null) {
                throw new FlowableIllegalArgumentException("No event object was passed to the consumer");
            } else {
                throw new FlowableIllegalArgumentException("Unsupported event object type: " + event.getEventObject().getClass());
            }
        }
        return eventRegistryProcessingInfo;
    }

    @Override
    public String getConsumerKey() {
        return "reviewEventConsumer";
    }
}
