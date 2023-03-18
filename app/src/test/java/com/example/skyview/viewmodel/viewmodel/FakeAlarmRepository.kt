import com.example.skyview.database.AlarmDAO
import com.example.skyview.model.AlarmPojo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAlarmRepository : AlarmDAO {

    private val alarms = mutableListOf<AlarmPojo>()

    fun setAlarms(newAlarms: List<AlarmPojo>) {
        alarms.clear()
        alarms.addAll(newAlarms)
    }

    override fun getAllAlarmsList(): Flow<List<AlarmPojo>> {
        return flow { emit(alarms) }
    }

    override fun insertAlarm(alarmPojo: AlarmPojo) {
        alarms.add(alarmPojo)
    }

    override fun deleteAlarm(alarmPojo: AlarmPojo) {
        alarms.remove(alarmPojo)
    }

}
