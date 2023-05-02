package sample;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class DataService extends ScheduledService<String> {
    @Override
    protected Task<String> createTask() {
        return new DataReceiver();
    }
}
