package com.rishabh.codexapplication.ui.tasklist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rishabh.codexapplication.domain.model.Todo
import com.rishabh.codexapplication.ui.theme.CodexApplicationTheme

@Composable
fun TaskListRoute(
    onAddTask: () -> Unit,
    onEditTask: (Long) -> Unit,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TaskListScreen(
        uiState = uiState,
        onAddTask = onAddTask,
        onEditTask = onEditTask,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onCheckedChange = viewModel::setCompleted
    )
}

@Composable
fun TaskListScreen(
    uiState: TaskListUiState,
    onAddTask: () -> Unit,
    onEditTask: (Long) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onCheckedChange: (Long, Boolean) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTask,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                },
                text = { Text(text = "New task") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tasks",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 28.sp,
                    modifier = Modifier.weight(1f)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    top = 8.dp,
                    bottom = 112.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TodaySummaryCard(
                        openCount = uiState.openCount,
                        completedCount = uiState.completedCount
                    )
                }
                item {
                    SearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = onSearchQueryChanged
                    )
                }
                if (uiState.visibleTasks.isEmpty()) {
                    item {
                        EmptyTaskState(
                            message = if (uiState.searchQuery.isBlank()) {
                                "No tasks yet. Tap New task to add one."
                            } else {
                                "No tasks match your search."
                            }
                        )
                    }
                } else {
                    item {
                        TaskListContainer(
                            tasks = uiState.visibleTasks,
                            onEditTask = onEditTask,
                            onCheckedChange = onCheckedChange
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TodaySummaryCard(openCount: Int, completedCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "TODAY",
            color = Color(0xFF4F378B),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.48.sp,
            lineHeight = 16.sp
        )
        Text(
            text = "$openCount open tasks",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 28.sp,
            lineHeight = 36.sp
        )
        Text(
            text = "$completedCount completed offline and saved locally",
            color = Color(0xFF4F378B),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp)
        )
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = "Search tasks",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TaskListContainer(
    tasks: List<Todo>,
    onEditTask: (Long) -> Unit,
    onCheckedChange: (Long, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(
                border = BorderStroke(1.dp, Color(0xFFE7E0EC)),
                shape = RoundedCornerShape(24.dp)
            )
            .background(Color(0xFFF7F2FA))
    ) {
        tasks.forEachIndexed { index, task ->
            TaskRow(
                task = task,
                onClick = { onEditTask(task.id) },
                onCheckedChange = { checked -> onCheckedChange(task.id, checked) }
            )
            if (index < tasks.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(start = 56.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: Todo,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PaperCheckbox(
            checked = task.isCompleted,
            onCheckedChange = onCheckedChange
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = task.title,
                color = if (task.isCompleted) Color(0xFF79747E) else MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
            Text(
                text = task.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun PaperCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (checked) MaterialTheme.colorScheme.primary else Color.Transparent)
            .border(
                width = if (checked) 0.dp else 2.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Text(
                text = "✓",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun EmptyTaskState(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF7F2FA),
        border = BorderStroke(1.dp, Color(0xFFE7E0EC))
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun TaskListScreenPreview() {
    CodexApplicationTheme {
        TaskListScreen(
            uiState = TaskListUiState(
                tasks = listOf(
                    Todo(1, "Wire Room entities", "Entity, DAO, Database"),
                    Todo(2, "Create Hilt module", "Repository binding", true),
                    Todo(3, "Build detail form state", "Title and description"),
                    Todo(4, "Connect NavHost routes", "List to editor")
                )
            ),
            onAddTask = {},
            onEditTask = {},
            onSearchQueryChanged = {},
            onCheckedChange = { _, _ -> }
        )
    }
}
