package com.example.passivepmvc.todoapp.edittask;

import com.example.passivepmvc.todoapp.R;
import com.example.passivepmvc.todoapp.model.Task;

/**
 * @author Christian Sarnataro
 *         Created on 05/08/16.
 */
public class EditTaskController implements EditTaskView.OnSaveTaskButtonListener {

    protected EditTaskView view;
    protected EditTaskControllerListener listener;
    private Long editedTaskId;


    public EditTaskController(EditTaskView view, Long editedTaskId, EditTaskControllerListener listener) {
        this.view = view;
        this.editedTaskId = editedTaskId;
        this.listener = listener;
        this.view.initComponents();
        this.initListeners(this.view);
        setToolbarTitle();
        initFieldsInView();
    }

    private void setToolbarTitle() {
        if (editedTaskId == null) {
            view.setToolbarTitle(view.getResources().getString(R.string.add_task));
        } else {
            view.setToolbarTitle(view.getResources().getString(R.string.edit_task));
        }
    }

    private void initListeners(EditTaskView view) {
        view.setSaveTaskButtonListener(this);
    }

    public void onHomeButtonClicked() {
        view.showMenu();
    }

//    private void initFieldsInView() {
//        if (isNewTask()) {
//            view.resetFields();
//        } else {
//            Task task = Task.get(editedTaskId);
//            view.setTitle(task.title);
//            view.setDescription(task.description);
//        }
//    }

    @Override
    public void onSaveTaskButtonClicked() {
        if (isNewTask()) {
            createTask(
                    view.getTitle(),
                    view.getDescription()
            );
        } else {
            updateTask(
                    editedTaskId,
                    view.getTitle(),
                    view.getDescription()
            );
        }
    }

    public void createTask(String title, String description) {
        Task newTask = new Task(title, description);
        if (newTask.isEmpty()) {
            view.showEmptyTaskError();
        } else {
            newTask.save();
            view.showTaskCreatedMessage();
            listener.onTaskCreated();

        }
    }

    public void updateTask(Long id, String title, String description) {
        if (id == null) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
        Task existingTask = Task.get(editedTaskId);
        existingTask.title = title;
        existingTask.description = description;
        existingTask.save(); // After an edit, go back to the list.
        view.showTaskUpdatedMessage();
        listener.onTaskUpdated();
    }

    private boolean isNewTask() {
        return editedTaskId == null;
    }


    private void initFieldsInView() {
        if (isNewTask()) {
            view.resetFields();
        } else {
            Task task = Task.get(editedTaskId);
            view.setTitle(task.title);
            view.setDescription(task.description);
        }
    }

    public void onDeleteMenuSelected() {
        Task task = Task.findById(Task.class, editedTaskId);
        task.delete();
        view.showTaskDeletedMessage();
        listener.onTaskDeleted();

    }


    public interface EditTaskControllerListener {
        void onTaskCreated();
        void onTaskUpdated();
        void onTaskDeleted();
    }
}
