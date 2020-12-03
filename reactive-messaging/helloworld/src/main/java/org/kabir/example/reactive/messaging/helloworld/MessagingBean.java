package org.kabir.example.reactive.messaging.helloworld;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
@ApplicationScoped
public class MessagingBean {

    @Inject
    ExternalAsyncResource producer;

    @Outgoing("invm")
    public CompletionStage<String> sendInVm() {
        return producer.getNextValue();
    }

    @Incoming("invm")
    public CompletionStage<Void> consumeInVm(Message<String> message) {
        String payload = message.getPayload();
        System.out.println("Received InVm: " + payload);
        return message.ack();
    }


}
