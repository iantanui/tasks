package com.example.tasks

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    var tasks by remember { mutableStateOf(listOf("Task1", "Task 2", "Tsk 3")) }
    var isAddTaskDialogOpen by remember { mutableStateOf(false) }

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

                TasksList(tasks = tasks)
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
        floatingActionButtonPosition = FabPosition.End,
        content = {
            if (isAddTaskDialogOpen) {
                AddTaskDialog(
                    onTaskAdded = { newTask ->
                        tasks = tasks + newTask
                        isAddTaskDialogOpen = false
                    },
                    onDismiss = { isAddTaskDialogOpen = false }

                )
            }
        }
    )
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
fun TasksList(tasks: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(tasks.size) { index ->
            TaskItem(taskName = tasks[index])
        }
    }
}

@Composable
fun TaskItem(taskName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = taskName,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TasksTheme {
        TaskApp()
    }
}