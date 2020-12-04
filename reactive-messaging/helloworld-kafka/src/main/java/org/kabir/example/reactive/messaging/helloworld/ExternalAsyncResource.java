package org.kabir.example.reactive.messaging.helloworld;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

/**
 * Class simulating external data being made available asynchronously.
 * In the real world this could be for example a REST client making
 * asynchronous calls
 *
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
@ApplicationScoped
public class ExternalAsyncResource {
    private final String[] values = new String[]{
            "Hello",
            "World",
            "Reactive",
            "Messaging"
    };

    private ScheduledExecutorService delayedExecutor = Executors.newSingleThreadScheduledExecutor(Executors.defaultThreadFactory());
    private final AtomicInteger count = new AtomicInteger(0);

    @PreDestroy
    public void stop() {
        delayedExecutor.shutdown();
    }

    public CompletionStage<String> getNextValue() {
        CompletableFuture<String> cf = new CompletableFuture<>();
        delayedExecutor.schedule(new NextOrRandom(cf), 2, TimeUnit.SECONDS);
        return cf;
    }

    private class NextOrRandom implements Runnable {
        private final CompletableFuture<String> cf;

        public NextOrRandom(CompletableFuture<String> cf) {
            this.cf = cf;
        }

        @Override
        public void run() {
            final int index = count.getAndIncrement();
            if (index < values.length) {
                cf.complete(values[index]);
            } else {
                int random = (int)(Math.random() * values.length);
                cf.complete(values[random]);
            }
        }
    }
}