package com.example.passivepmvc.todoapp.tasks;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passivepmvc.todoapp.R;
import com.example.passivepmvc.todoapp.model.Task;
import com.example.passivepmvc.todoapp.ui.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Sarnataro
 *         Created on 05/08/16.
 */
public class TasksView extends DrawerLayout {

    private Toolbar toolbar;
    private ActionBar ab;

    private FloatingActionButton addTaskButton;
    private ScrollChildSwipeRefreshLayout refreshLayout;
    private TasksAdapter tasksAdapter;
    private View tasksListContainer;
    private View noTasksView;
    private ImageView noTaskIcon;
    private TextView noTaskMainView;
    private TextView noTaskAddView;
    private TextView filteringLabelView;
    private RecyclerView tasksList;

    private FilterMenuListener filterMenuListener;

    public TasksView(Context context) {
        super(context);
    }

    public TasksView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TasksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** Initialise widgets using findViewById */
    public void initComponents() {
        setUpToolbar();
        setUpActionBar();

        addTaskButton = (FloatingActionButton) findViewById(R.id.fab_add_task);
        refreshLayout = (ScrollChildSwipeRefreshLayout) findViewById(R.id.refresh_layout);
        tasksListContainer = findViewById(R.id.tasks_list_container);
        noTaskAddView = (TextView) findViewById(R.id.noTasksAdd);
        noTasksView = findViewById(R.id.noTasks);
        noTasksView = findViewById(R.id.noTasks);
        noTaskIcon = (ImageView) findViewById(R.id.noTasksIcon);
        noTaskMainView = (TextView) findViewById(R.id.noTasksMain);
        filteringLabelView = (TextView) findViewById(R.id.filteringLabel);

        initTaskList();


    }

    private void initTaskList() {
        tasksAdapter = new TasksAdapter(new ArrayList<Task>(0));
        tasksList = (RecyclerView) findViewById(R.id.tasks_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tasksList.setLayoutManager(layoutManager);
        tasksList.setAdapter(tasksAdapter);

    }


    public void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.list_title));
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
    }

    public void setUpActionBar() {
        ab = ((AppCompatActivity) getContext()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

    }

    public void showMenu() {
        this.openDrawer(GravityCompat.START);

    }

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showTasks(List<Task> tasks) {
        tasksAdapter.refreshData(tasks);
        tasksListContainer.setVisibility(View.VISIBLE);
        noTasksView.setVisibility(View.GONE);
    }

    public void showActiveFilterLabel() {
        filteringLabelView.setText(getResources().getString(R.string.label_active));
    }

    public void showCompletedFilterLabel() {
        filteringLabelView.setText(getResources().getString(R.string.label_completed));
    }

    public void showAllFilterLabel() {
        filteringLabelView.setText(getResources().getString(R.string.label_all));
    }

    public void showNoActiveTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_active),
                R.drawable.ic_check_circle_24dp,
                false
        );
    }

    public void showNoTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }

    public void showNoCompletedTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_completed),
                R.drawable.ic_verified_user_24dp,
                false
        );
    }


    public void showTaskMarkedComplete() {
        Snackbar.make(this, getResources().getString(R.string.task_marked_complete), Snackbar.LENGTH_SHORT)
                .show();
    }

    public void showTaskMarkedActive() {
        Snackbar.make(this, getResources().getString(R.string.task_marked_active), Snackbar.LENGTH_SHORT)
                .show();
    }


    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        tasksListContainer.setVisibility(View.GONE);
        noTasksView.setVisibility(View.VISIBLE);

        noTaskMainView.setText(mainText);
        noTaskIcon.setImageDrawable(getResources().getDrawable(iconRes));
        noTaskAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }


    public void setLoadingIndicator(boolean active) {
        refreshLayout.setRefreshing(active);
    }

    public void setOnTaskListRefreshListener(final TaskListRefreshListener listener) {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.onTaskListRefreshed();
            }
        });
    }

    public void setTasksItemListener(TaskItemListener taskItemListener) {
        tasksAdapter.setTaskItemListener(taskItemListener);
    }

    public void showFilteringPopUpMenu() {

        PopupMenu popup = new PopupMenu(getContext(), findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
                        filterMenuListener.onActiveTasksFilterSelected();
                        break;
                    case R.id.completed:
                        filterMenuListener.onCompletedTasksFilterSelected();
                        break;
                    default:
                        filterMenuListener.onAllTasksFilterSelected();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public void setFilterMenuListener(FilterMenuListener listener) {
        this.filterMenuListener = listener;
    }


    class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

        private List<Task> items;
        private TaskItemListener taskItemListener;

        public TasksAdapter(List<Task> items) {
            this.items = items;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

            final View view = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.tasks_list_item, parent, false);


            final ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.completedCheckBox = (CheckBox) view.findViewById(R.id.complete);

            final int position = viewType;

            viewHolder.completedCheckBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Task item = items.get(position);
                    taskItemListener.onCompleteTaskClick(item, viewHolder.completedCheckBox.isChecked());
                }
            });

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task item = items.get(position);
                    taskItemListener.onTaskClicked(item);
                }
            });

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            final Task item = items.get(position);

            viewHolder.completedCheckBox.setChecked(item.completed);
            viewHolder.title.setText(item.getTitleForList());

        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public List<Task> getItems() {
            return items;
        }

        public void setTaskItemListener(TaskItemListener taskItemListener) {
            this.taskItemListener = taskItemListener;
        }

        public void refreshData(List<Task> tasks) {
            this.items = tasks;
            notifyDataSetChanged();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            CheckBox completedCheckBox;

            public ViewHolder(View itemView) {
                super(itemView);
            }

        }
    }

    public void setAddTaskButtonListener(final AddTaskListener listener) {
        addTaskButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddTaskButtonClicked();
            }
        });
    }

    public interface AddTaskListener {
        void onAddTaskButtonClicked();
    }

    interface TaskItemListener {
        void onTaskClicked(Task clickedTask);
        void onCompleteTaskClick(Task task, boolean isChecked);
    }

    public interface TaskListRefreshListener {
        void onTaskListRefreshed();
    }

    public interface FilterMenuListener {
        void onAllTasksFilterSelected();
        void onCompletedTasksFilterSelected();
        void onActiveTasksFilterSelected();
    }


}
