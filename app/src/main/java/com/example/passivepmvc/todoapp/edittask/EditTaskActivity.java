package com.example.passivepmvc.todoapp.edittask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.passivepmvc.todoapp.R;

public class EditTaskActivity extends AppCompatActivity implements EditTaskController.EditTaskControllerListener {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";
    private EditTaskController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task_activity);

        Long editedTaskId = null;
        if ( getIntent().getExtras() != null && getIntent().getExtras().containsKey(ARGUMENT_EDIT_TASK_ID)) {
            editedTaskId = getIntent().getExtras().getLong(ARGUMENT_EDIT_TASK_ID);
        }

        controller = new EditTaskController((EditTaskView) findViewById(R.id.edit_task_view), editedTaskId, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.edit_task_fragment_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                controller.onHomeButtonClicked();
                return true;
            case R.id.menu_delete:
                controller.onDeleteMenuSelected();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCreated() {
        this.finish();
    }

    @Override
    public void onTaskUpdated() {
        this.finish();
    }

    @Override
    public void onTaskDeleted() {
        this.finish();
    }
}
