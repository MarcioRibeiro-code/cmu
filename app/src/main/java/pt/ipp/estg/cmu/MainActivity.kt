package pt.ipp.estg.cmu

//import pt.ipp.estg.cmu.ui.screen.ExercisesScreen
import MapScreen
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoorBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import pt.ipp.estg.cmu.components.providers.WorkoutFlowIconProvider
import pt.ipp.estg.cmu.navigation.WorkoutFlow.AppRoute
import pt.ipp.estg.cmu.ui.screen.BookmarksScreen
import pt.ipp.estg.cmu.ui.screen.ExercisesScreen
import pt.ipp.estg.cmu.ui.screen.HistoryExercisesContent
import pt.ipp.estg.cmu.ui.screen.HomeScreen
import pt.ipp.estg.cmu.ui.screen.PlannedExercisesScreen
import pt.ipp.estg.cmu.ui.screen.ProfileScreen
import pt.ipp.estg.cmu.ui.theme.CmuTheme
import pt.ipp.estg.cmu.viewmodel.LocationViewModel

class MainActivity : ComponentActivity() {

    //Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    //Location
    private val locationViewModel: LocationViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var listeningToUpdates = false


    companion object {
        const val APP_NAME = "Fitness App"
        const val CHANNEL_NAME = "Fitness App Channel"
        const val CHANNEL_ID = "fitness_app_channel"
        const val NOTIFICATION_ID = 12345
        const val REQUEST_CODE_LOCATION = 0
        const val REQUEST_CODE_NOTIFICATION = 1
        private const val REQUEST_LOCATION_PERMISSION = 123
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.locations.first()
            locationViewModel.updateLocation(location)
            Log.d("Location", "Location updated: $location")
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // Verifica se o usuário está autenticado
        if (firebaseAuth.currentUser == null) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()  // Finaliza a MainActivity se o usuário não estiver autenticado
            return
        }


        //Location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startUpdatingLocation()


        setContent {
            CmuTheme(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = true
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Scaffold(topBar = {
                        TopBar(
                            navController = navController,
                            firebaseAuth = firebaseAuth
                        )
                    },
                        bottomBar = { NavigationBar(navController = navController) })
                    { innerPadding ->
                        NavigationHost(
                            navHostController = navController,
                            innerPadding = innerPadding,
                            locationViewModel = locationViewModel
                        )
                    }

                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(
        navController: NavHostController,
        firebaseAuth: FirebaseAuth
    ) {
        // NavController#findTitle
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val topBarLabel = AppRoute.values().firstOrNull { screen ->
            currentDestination?.hierarchy?.any { it.route == screen.route } == true
        }?.topBarLabel

        // AuthActivity
        val context = LocalContext.current
        val activity = (LocalContext.current as? Activity)
        val intent = Intent(context, AuthActivity::class.java)

        TopAppBar(
            title = {
                Text(text = topBarLabel ?: "Fitness App", fontWeight = FontWeight.ExtraBold)
            },
            navigationIcon = {
                if (navController.graph.findStartDestination().id != currentDestination?.id) {
                    FilledTonalIconButton(onClick = { navController.popBackStack() }) {
                        Icon(WorkoutFlowIconProvider.back, contentDescription = "Back")
                    }
                }
            },
            actions = {
                FilledTonalIconButton(onClick = {
                    firebaseAuth.signOut()
                    startActivity(intent)
                    activity?.finish()
                }) {
                    Icon(Icons.Outlined.DoorBack, contentDescription = "Sign Out")
                }
            }
        )
    }


    @Composable
    fun NavigationHost(
        navHostController: NavHostController,
        innerPadding: PaddingValues,
        locationViewModel: LocationViewModel
    ) {
        NavHost(
            navController = navHostController,
            startDestination = AppRoute.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoute.HomeScreen.route) { HomeScreen(navHostController) }
            composable(AppRoute.ExercisesScreen.route) { PlannedExercisesScreen() }
            composable(
                "${AppRoute.ExercisesScreen.route}/{exerciseId}"
            ) { navBackStackEntry ->
                val exerciseId = navBackStackEntry.arguments?.getString("exerciseId")
                println(exerciseId)
                ExercisesScreen(navHostController, exerciseId)
            }
            composable(AppRoute.SearchPlaces.route) {
                MapScreen(navHostController, locationViewModel)
            }
            composable(AppRoute.ProfileScreen.route) { ProfileScreen() }
            composable(AppRoute.BookmarkersScreen.route) { BookmarksScreen() }
            composable(AppRoute.HistoryScreen.route) { HistoryExercisesContent() }
            /*ADICIONAR AQUI OS COMPOSABLES DOS OUTROS SCREENS
            * */
        }
    }


    @Composable
    fun NavigationBar(navController: NavHostController) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        androidx.compose.material3.NavigationBar {
            AppRoute.values().forEachIndexed { _, screen ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == screen.route } == true

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.navigationLabel
                        )

                    },
                    label = {
                        Text(
                            text = screen.navigationLabel,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    selected = selected,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startUpdatingLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 120000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(120000)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnSuccessListener {
            listeningToUpdates = true
        }.addOnFailureListener { e ->
            Log.e("Location", "Error requesting location updates: ${e.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
        if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
        if (checkSelfPermission(POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATION)
        }
        createNotificationChannel()
    }

    @SuppressLint("MissingPermission")
    override fun onStop() {
        super.onStop()
        if (listeningToUpdates) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }

        // Notification Alert
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.icone)
            .setContentTitle(APP_NAME)
            .setContentText("Thank you for using Fitness APP!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            recreate()
        }
    }
}
