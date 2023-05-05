package sample;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;


/**
 * Creates new receiver
 *
 * @author Jay Staddon
 * @version 4th May 2023
 */
public class DataService extends ScheduledService<String> {
    @Override
    protected Task<String> createTask() {
        return new DataReceiver();
    }
}
