package com.hnsh.dialogue.services
import com.dosmono.logger.Logger
import com.hnsh.dialogue.bean.cbs.SynchTask
import com.hnsh.dialogue.constants.TSRConstants
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

abstract class SynchManager {

    private var isRunning = AtomicBoolean(false)
    private val synchTask = ConcurrentHashMap<String, SynchTask>() //mutableMapOf<Long, SynchTask>() //SparseArray<SynchTask>()

    private var timer: Timer? = null

    private fun startSynch() {
        if(isRunning.compareAndSet(false, true)) {
            timer?.cancel()

            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    if(!checkTask()) stopSynch()
                }
            }, TSRConstants.SYNCH_PERIOD, TSRConstants.SYNCH_PERIOD)
        }
    }

    private fun stopSynch() {
        isRunning.set(false)
        timer?.cancel()
    }

    private fun checkTask(): Boolean {
        var hasRefresh = false
        when(synchTask.isNotEmpty()) {
            true -> {
                val expired = mutableListOf<String>()
                val refresh = mutableListOf<String>()

                val clock = System.currentTimeMillis()
                for((_, task) in synchTask) {
                    if(clock - task.sendTime < TSRConstants.PUSH_TIMEOUT) {
                        hasRefresh = true
                        refresh.add(task.msgId)
                    }else {
                        expired.add(task.msgId)
                        onTimeout(task.sessionId, task.query)
                    }
                }

                if(expired.count() > 0) {
                    for(item in expired) {
                        synchTask.remove(item)
                    }
                }

                if(hasRefresh) {
                    onRefresh(refresh)
                }
            }
        }
        return hasRefresh
    }

    fun addTask(task: SynchTask) {
        synchTask[task.msgId] = task
        Logger.i("synch task count: ${synchTask.size}, task : $task")

        checkTask()
        startSynch()
    }

    fun getAndRemoveTask(msgId: String): SynchTask? {
        return synchTask.remove(msgId)
    }

    abstract fun onRefresh(list: List<String>)
    abstract fun onTimeout(session: Int, query: String)
}