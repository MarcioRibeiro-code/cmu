package pt.ipp.estg.cmu

import androidx.activity.ComponentActivity

class ExercisesActivity : ComponentActivity() {
/*

    private lateinit var firebaseAuth: FirebaseAuth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        setContent {
            CmuTheme {
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
                            innerPadding = innerPadding
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
                Text(text = topBarLabel ?: "Chargify", fontWeight = FontWeight.ExtraBold)
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
    ) {
        NavHost(
            navController = navHostController,
            startDestination = AppRoute.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoute.HomeScreen.route) { HomeScreen(HomeViewModel()) }
            composable(AppRoute.pt.ipp.estg.cmu.ui.screen.ExercisesScreen.route) {  }
            /*ADICIONAR AQUI OS COMPOSABLES DOS OUTROS SCREENS
            * */
        }
    }


    @Composable
    fun NavigationBar(navController: NavHostController) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NavigationBar {
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
    }*/
}



