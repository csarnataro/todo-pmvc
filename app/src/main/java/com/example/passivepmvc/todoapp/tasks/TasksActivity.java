package com.example.passivepmvc.todoapp.tasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.passivepmvc.todoapp.R;
import com.example.passivepmvc.todoapp.edittask.EditTaskActivity;

public class TasksActivity extends AppCompatActivity implements TasksController.TasksControllerListener {

    private TasksController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_activity);

        controller = new TasksController((TasksView) findViewById(R.id.tasks_view), this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                controller.onHomeButtonClicked();
                return true;
            case R.id.menu_filter:
                controller.showFilteringMenu();
                break;
            case R.id.menu_clear:
                controller.onClearMenuSelected();
                break;
            case R.id.menu_refresh:
                controller.onRefreshMenuSelected();
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.loadTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onAddTaskButtonClicked() {
        Intent intent = new Intent(this, EditTaskActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onTaskClicked(Long itemId) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(EditTaskActivity.ARGUMENT_EDIT_TASK_ID, itemId);
        this.startActivity(intent);
    }
}
