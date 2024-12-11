package com.example.app3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoApp()
        }
    }
}

data class Task(
    val id: Int,
    val title: String,
    var priority: String,
    var isCompleted: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoApp() {
    val tasks = remember {
        mutableStateListOf(
            Task(1, "Comprar leche", "Baja", false),
            Task(2, "Cambiar bombilla", "Media", false),
            Task(3, "Terminar el libro", "Baja", true),
            Task(4, "Estudiar", "Alta", false)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista de Tareas", fontSize = 24.sp) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text("Tareas Pendientes", style = MaterialTheme.typography.titleMedium)
            }
            items(tasks.filter { !it.isCompleted }) { task ->
                TaskCard(task, tasks)
            }

            item {
                Divider(Modifier.padding(vertical = 8.dp))
            }
            item {
                Text("Tareas Completadas", style = MaterialTheme.typography.titleMedium)
            }
            items(tasks.filter { it.isCompleted }) { task ->
                TaskCard(task, tasks)
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, tasks: MutableList<Task>) {
    var expanded by remember { mutableStateOf(false) }
    var showPriorityMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Badge(
                modifier = Modifier.padding(end = 16.dp),
                containerColor = when (task.priority) {
                    "Alta" -> Color.Red
                    "Media" -> Color.Yellow
                    else -> Color.Green
                }
            ) {
                Text(
                    text = when (task.priority) {
                        "Alta" -> "!"
                        "Media" -> "~"
                        else -> "-"
                    },
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Prioridad: ${task.priority}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                )
            }

            if (task.isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completada",
                    tint = Color.Green,
                    modifier = Modifier.size(24.dp).padding(end = 8.dp)
                )
            }

            Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(if (task.isCompleted) "Marcar como pendiente" else "Marcar como completada") },
                        onClick = {
                            task.isCompleted = !task.isCompleted
                            tasks.remove(task)
                            tasks.add(task)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Cambiar prioridad") },
                        onClick = {
                            expanded = false
                            showPriorityMenu = true
                        }
                    )
                }

                if (showPriorityMenu) {
                    DropdownMenu(
                        expanded = showPriorityMenu,
                        onDismissRequest = { showPriorityMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Alta") },
                            onClick = {
                                task.priority = "Alta"
                                showPriorityMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Media") },
                            onClick = {
                                task.priority = "Media"
                                showPriorityMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Baja") },
                            onClick = {
                                task.priority = "Baja"
                                showPriorityMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}
