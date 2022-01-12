package com.home.opencarshare.test

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.home.opencarshare.App
import com.home.opencarshare.di.NetworkModule
import com.home.opencarshare.model.pojo.DriverCredentials
import com.home.opencarshare.navigation.AppNavigation
import com.home.opencarshare.network.Repository
import com.home.opencarshare.network.Response
import com.home.opencarshare.persistent.DriverPreferences
import com.home.opencarshare.persistent.PreferenceRepository
import com.home.opencarshare.screens.DriverScreen
import com.home.opencarshare.screens.TripCreateScreenLauncher
import com.home.opencarshare.screens.viewmodel.DriverViewModel
import com.home.opencarshare.screens.viewmodel.TripsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun TestScreen(viewModel: TripsViewModel) {

//    val driverState by viewModel.newDriverState.collectAsState()
//    val driverState by viewModel.newCreateTripUiState.collectAsState()
//    val driverState by viewModel.uiState.collectAsState()

    Button(onClick = {/*viewModel.getDriver()*/}) {
/*        if (driverState is Response.Data) {
            Text(text = "Driver: ${(driverState as Response.Data).data.name}")
        }*/
//        Text(text = "Driver: ${driverState.driver}")
    }

}
@AndroidEntryPoint
class TestActivity : AppCompatActivity() {

    /**
     * TODO: isolate issue
     * - run single Driver screen
     * - get reference on the driver state and run Driver screen
     * - get reference on the driver state and initiate its update, then run Driver screen
     * */

    private val viewModel: TripsViewModel by viewModels()
    private val driverViewModel: DriverViewModel by viewModels()

    private lateinit var repo: Repository
    private lateinit var preferenceRepository: PreferenceRepository
    private lateinit var driverPreferencesFlow: Flow<DriverPreferences>
    private val _driverState = MutableStateFlow(DriverCredentials())
    val driverState: StateFlow<DriverCredentials> = _driverState

    // TODO verify UI logic with _newDriverState
    private val _newDriverState = MutableStateFlow(Response.Loading(DriverCredentials()) as Response<DriverCredentials>)
    val newDriverState: StateFlow<Response<DriverCredentials>> = _newDriverState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceRepository = PreferenceRepository(applicationContext)
        driverPreferencesFlow = preferenceRepository.driverPreferencesFlow
        val module = NetworkModule()
        repo = Repository(module.provideApi(this, module.provideClient(module.provideInterceptor(applicationContext))))

//        runDriverScenario()
    }

    fun runDriverScenario() {
        setContent {
            TripCreateScreenLauncher(viewModel = driverViewModel)
        }
    }

/*    fun runAuthScenario() {
        viewModel.createDriver(
            driverCredentials = DriverCredentials(
                name = "Joe Dow",
                cell = "+79808007060",
                secret = "clean_blood",
                tripsCount = "0"
            ))
    }

    fun runOneScenario() {
        viewModel.createDriver(DriverCredentials(name = "Joe", cell = "123"))
//        driverViewModel.createDriver(DriverCredentials(name = "Joe", cell = "123"))

        setContent {
            TestScreen(viewModel = viewModel)
        }
    }*/

    fun runAnotherScenario() {
        setContent {
            DriverScreen(viewModel = driverViewModel)
        }
    }


    /*        lifecycleScope.launchWhenResumed {
            Log.d(App.TAG, "New request for driver")
            driverPreferencesFlow
                    .stateIn(lifecycleScope)
                    .flatMapConcat { it ->
                        repo.getDriver(it.driver.cell, it.driver.secret)
                    }
*//*                    .transform<DriverPreferences, Flow<Response<DriverCredentials>>> { it ->
                        Log.d(App.TAG, "Got driver from cache, ask for driver from server")
                        emit(repo.getDriver(it.driver.cell, it.driver.secret))
                    }*//*
                    .onStart {
                    }
                    .onCompletion { e ->
                    }
                    .catch { e ->
                        Log.e(App.TAG, "Something went wrong on loading with driver", e)
                        Response.Error.Exception(e)
                    }
                    .collect { it ->
                        Log.d(App.TAG, "Got driver from server")
                        Log.d(App.TAG, "Collect new driver state ${it.javaClass}")
                        _newDriverState.value = it
*//*                        it.collect { it ->
                            Log.d(App.TAG, "Collect new driver state ${it.javaClass}")
                            _newDriverState.value = it
                        }*//*
//                        _newDriverState.value = it
                    }
        }*/

}