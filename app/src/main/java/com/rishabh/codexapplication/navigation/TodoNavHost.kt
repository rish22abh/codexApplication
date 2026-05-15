package com.rishabh.codexapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rishabh.codexapplication.ui.taskdetail.TaskDetailRoute
import com.rishabh.codexapplication.ui.tasklist.TaskListRoute

private object Routes {
    const val TaskList = "tasks"
    const val TaskDetail = "task-detail"
    const val TaskId = "taskId"
}

@Composable
fun TodoNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.TaskList
    ) {
        composable(Routes.TaskList) {
            TaskListRoute(
                onAddTask = { navController.navigate(Routes.TaskDetail) },
                onEditTask = { id -> navController.navigate("${Routes.TaskDetail}?${Routes.TaskId}=$id") }
            )
        }
        composable(
            route = "${Routes.TaskDetail}?${Routes.TaskId}={${Routes.TaskId}}",
            arguments = listOf(
                navArgument(Routes.TaskId) {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) {
            TaskDetailRoute(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
