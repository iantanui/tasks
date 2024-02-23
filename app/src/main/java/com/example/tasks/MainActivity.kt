package com.example.tasks

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.tasks.ui.theme.TasksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasksTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskApp()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskApp() {
    var isAddTaskDialogOpen by remember { mutableStateOf(false) }
    var completedTasks by remember { mutableStateOf(listOf<String>()) }
    var incompleteTasks by remember { mutableStateOf(listOf<String>()) }
    var showCompletedTasks by remember { mutableStateOf(false) }

    // callback for incomplete tasks
    val onDeleteIncompleteClicked: (String) -> Unit = { taskName ->
        incompleteTasks = incompleteTasks.filter { it != taskName }
    }

    // callback  for complete tasks
    val onDeleteCompletedClicked: (String) -> Unit = { taskName ->
        completedTasks = completedTasks.filter { it != taskName }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                TopAppBar(
                    title = {
                        Text(
                            text = "Tasks"
                        )
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            // Icon for google profile
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile"
                            )
                        }
                    }
                )

                Divider() // Divider line

            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Handle add task
                    isAddTaskDialogOpen = true
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .padding(top = 60.dp)
        ) {
            if (isAddTaskDialogOpen) {
                AddTaskDialog(
                    onTaskAdded = { newTask ->
                        incompleteTasks = listOf(newTask) + incompleteTasks
                        isAddTaskDialogOpen = false
                    },
                    onDismiss = { isAddTaskDialogOpen = false }

                )
            }

            // Display incomplete
            TasksList(
                tasks = incompleteTasks,
                onTaskCompleted = { taskName ->
                    // Move completed tasks to completedTasks list
                    val taskToMove = incompleteTasks.find { it == taskName }
                    if (taskToMove != null) {
                        incompleteTasks = incompleteTasks - taskToMove
                        completedTasks = listOf(taskName) + completedTasks
                    }
                },
                onEditClicked = {
                    //
                },
                onDeleteClicked = onDeleteIncompleteClicked
            )

            // Display completed tasks section only if there are completed tasks available
            if (completedTasks.isNotEmpty()) {
                Divider()

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { showCompletedTasks = !showCompletedTasks }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = if (showCompletedTasks) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle Completed Tasks",
                        modifier = Modifier
                            .clickable { showCompletedTasks = !showCompletedTasks }
                    )
                }

                // Display complete tasks is showCompletedTasks is true
                if (showCompletedTasks) {
                    TasksList(
                        tasks = completedTasks,
                        onTaskCompleted = { taskName ->
                            val taskToMove = completedTasks.find { it == taskName }
                            if (taskToMove != null) {
                                completedTasks = completedTasks - taskToMove
                                incompleteTasks = incompleteTasks + listOf(taskName)
                            }
                        },
                        onEditClicked = {
                            //
                        },
                        onDeleteClicked = onDeleteCompletedClicked
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(onTaskAdded: (String) -> Unit, onDismiss: () -> Unit) {
    var newTaskText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = { Text(text = "Add Task") },
        text = {
            TextField(
                value = newTaskText,
                onValueChange = { newTaskText = it },
                label = { Text(text = "Task Name") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    /*TODO*/
                    onTaskAdded(newTaskText)
                    newTaskText = ""
                    onDismiss()
                }
            ) {
                Text("Add")
            }
        }
    )
}

@Composable
fun TasksList(
    tasks: List<String>,
    onTaskCompleted: (String) -> Unit,
    onEditClicked: (String) -> Unit,
    onDeleteClicked: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(tasks.size) { index ->
            val task = tasks[index]
            TaskItem(
                taskName = task,
                isCompleted = false,
                onTaskClicked = { onTaskCompleted(task) },
                onEditClicked = { onEditClicked(task) },
                onDeleteClicked = { onDeleteClicked(task) }
            )
        }
    }
}

@Composable
fun TaskItem(
    taskName: String,
    isCompleted: Boolean,
    onTaskClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {

    var isMenuOpen by remember { mutableStateOf(false) }
    val completed = remember { mutableStateOf(isCompleted) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Done icon for completed, circle for incomplete
            if (completed.value) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Completed",
                    tint = Color.Green,
                    modifier = Modifier
                        .size(20.dp)
                )
            } else {
                // incomplete
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Incomplete",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            completed.value = !completed.value
                            onTaskClicked()
                            Log.d("TaskItem","Icon clicked. Completed: ${completed.value}")
                        }
                )
            }

            // Task name (editable)
            Text(
                text = taskName,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )

            // Dropdown Menu button
            IconButton(onClick = { isMenuOpen = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }

            // Dropdown menu
            DropdownMenu(
                expanded = isMenuOpen,
                onDismissRequest = { isMenuOpen = false },
                offset = DpOffset(x = (-80).dp, y = 0.dp)
            ) {
                IconButton(onClick = { onEditClicked(); isMenuOpen = false }) {
                    Icon(Icons.Default.Edit, contentDescription = "edit")
                }

                IconButton(onClick = { onDeleteClicked(); isMenuOpen = false }) {
                    Icon(Icons.Default.Delete, contentDescription = "delete")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TasksTheme {
        TaskApp()
    }
}