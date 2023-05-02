package sample;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class dataService extends ScheduledService<String> {
    @Override
    protected Task<String> createTask() {
        return new DataReceiver();
    } //Creates new receiver
}
