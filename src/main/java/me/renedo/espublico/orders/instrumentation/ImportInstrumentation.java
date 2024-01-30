package me.renedo.espublico.orders.instrumentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import me.renedo.espublico.orders.domain.PageOfOrders;

public class ImportInstrumentation {
    private static final Logger log = LoggerFactory.getLogger(ImportInstrumentation.class);

    private final StopWatch timer;

    public ImportInstrumentation(StopWatch timer) {
        this.timer = timer;
        timer.start("page " + 0);
    }

    public void finish() {
        timer.stop();
        log.info(timer.prettyPrint());
    }

    public static ImportInstrumentation of(int pageSize) {
        log.info("Importing orders with page size {}", pageSize);
        return new ImportInstrumentation(new StopWatch());
    }

    public void registerError(Exception e) {
        log.error("Error importing page", e);
    }

    public void registerPageInformation(PageOfOrders page) {
        timer.stop();
        log.info("Importing orders with page {} with size {} in {}ms - Total {}ms", page.getPage(), page.getOrders().size(),
                timer.lastTaskInfo().getTimeMillis(), timer.getTotalTimeMillis());
        timer.start("page " + page.getPage());
    }
}
