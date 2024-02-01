package me.renedo.espublico.orders.instrumentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

public class ExportInstrumentation {
    private static final Logger log = LoggerFactory.getLogger(ExportInstrumentation.class);

    private final StopWatch timer;

    public ExportInstrumentation(StopWatch timer) {
        this.timer = timer;
        timer.start();
    }

    public void finish() {
        timer.stop();
        if (log.isInfoEnabled()) {
            log.info("Final result for every process: {}", timer.prettyPrint());
        }
    }

    public static ExportInstrumentation of(int pageSize) {
        log.info("Export orders with page size {}", pageSize);
        return new ExportInstrumentation(new StopWatch());
    }

    public void registerAnError(String message) {
        log.error(message);
    }

    public void registerPageInformation(int page, int size) {
        timer.stop();
        log.info("Export orders with page {} with size {} in {}ms - Total {}ms", page, size,
                timer.lastTaskInfo().getTimeMillis(), timer.getTotalTimeMillis());
        timer.start("page " + page);
    }
}
