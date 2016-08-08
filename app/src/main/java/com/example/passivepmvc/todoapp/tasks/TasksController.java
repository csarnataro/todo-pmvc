package com.example.passivepmvc.todoapp.tasks;

import android.util.Log;

import com.example.passivepmvc.todoapp.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Sarnataro
 *         Created on 05/08/16.
 */
public class TasksController implements
        TasksView.AddTaskListener,
        TasksView.TaskItemListener,
        TasksView.TaskListRefreshListener, TasksView.FilterMenuListener {

    private TasksView view;
    private TasksControllerListener listener;
    private List<Task> allTasks;
    private TasksFilterType currentFiltering = TasksFilterType.ALL_TASKS;

    public TasksController(TasksView view, TasksControllerListener listener) {
        this.view = view;
        this.listener = listener;

        this.view.initComponents();
        this.initListeners(this.view);

    }

    /** Set this as listener on view */
    private void initListeners(TasksView view) {
        view.setAddTaskButtonListener(this);
        view.setOnTaskListRefreshListener(this);
        view.setTasksItemListener(this);
        view.setFilterMenuListener(this);

    }

    public void onHomeButtonClicked() {
        view.showMenu();
    }


    public void loadTasks() {
        loadTasks(true);
    }

    private void loadTasks(final boolean showLoadingUI) {
        if (showLoadingUI) {
            view.setLoadingIndicator(true);
        }
        List<Task> tasks = Task.all();
        if (tasks != null && tasks.size() > 0) {
            Log.d("TASK", "-------- First task is: [" + tasks.get(0).toString() + "");
        }
        onTasksLoaded(tasks);
    }



    public void onTasksLoaded(List<Task> tasks) {
        allTasks = tasks;
        view.setLoadingIndicator(false);
        processTasks(getTasksToShow());
    }

    private List<Task> getTasksToShow() {
        List<Task> tasksToShow = new ArrayList<>();
        // We filter the tasks based on the requestType
        for (Task task : allTasks) {
            switch (currentFiltering) {
                case ALL_TASKS:
                    tasksToShow.add(task);
                    break;
                case ACTIVE_TASKS:
                    if (!task.completed) {
                        tasksToShow.add(task);
                    }
                    break;
                case COMPLETED_TASKS:
                    if (task.completed) {
                        tasksToShow.add(task);
                    }
                    break;
                default:
                    tasksToShow.add(task);
                    break;
            }
        }
        return tasksToShow;

    }

    private void processTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            view.showTasks(tasks);
            // Set the filter label's text.
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (currentFiltering) {
            case ACTIVE_TASKS:
                view.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                view.showCompletedFilterLabel();
                break;
            default:
                view.showAllFilterLabel();
                break;
        }
    }

    private void processEmptyTasks() {
        switch (currentFiltering) {
            case ACTIVE_TASKS:
                view.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                view.showNoCompletedTasks();
                break;
            default:
                view.showNoTasks();
                break;
        }
    }

    @Override
    public void onTaskListRefreshed() {
        loadTasks();
    }

    @Override
    public void onTaskClicked(Task clickedTask) {
        listener.onTaskClicked(clickedTask.id);

    }

    @Override
    public void onCompleteTaskClick(Task task, boolean isChecked) {
        task.completed = isChecked;
        task.save();

        if (isChecked) {
            view.showTaskMarkedComplete();
        } else {
            view.showTaskMarkedActive();
        }
        loadTasks(false);

    }

    public void onRefreshMenuSelected() {
        loadTasks(true);
    }

    public void onClearMenuSelected() {
        Task.deleteAll(Task.class, "completed = ?", "1");
        loadTasks();
    }

    public void showFilteredTasks(TasksFilterType type) {
        currentFiltering = type;
        processTasks(getTasksToShow());

    }

    public void showFilteringMenu() {
        view.showFilteringPopUpMenu();
    }

    @Override
    public void onAllTasksFilterSelected() {
        showFilteredTasks(TasksFilterType.ALL_TASKS);
    }

    @Override
    public void onCompletedTasksFilterSelected() {
        showFilteredTasks(TasksFilterType.COMPLETED_TASKS);

    }

    @Override
    public void onActiveTasksFilterSelected() {
        showFilteredTasks(TasksFilterType.ACTIVE_TASKS);

    }

    public enum TasksFilterType {
        /** Do not filter tasks. */
        ALL_TASKS,

        /** Filters only the active (not completed yet) tasks. */
        ACTIVE_TASKS,

        /** Filters only the completed tasks. */
        COMPLETED_TASKS
    }

    @Override
    public void onAddTaskButtonClicked() {
        if (Task.count(Task.class, null, null) <= 5) {
            listener.onAddTaskButtonClicked();
        } else {
            view.showMessage("With the free version you can create at most 5 to-dos.");
        }
    }

    public interface TasksControllerListener {
        void onAddTaskButtonClicked();
        void onTaskClicked(Long itemId);
    }
}
