package com.esie.math;
import com.esie.InitialisationInterface;
import com.esie.core.ioc.parents.ConstructThread;
import com.esie.math.Forms.MainForm;
import com.esie.math.Threads.Thread_0;
import com.esie.math.Threads.Thread_1;

final public class MathInit implements InitialisationInterface {

    public MathInit() {

    }

    /**
     * FORMS INITIALISATION
     *
     * @class_form(NAME)
     */

    @Override
    public void initForms() {
        new MainForm("MainForm");
    }

    /**
     * LOGIC THREADS INITIALISATION
     *
     * @ConstructThread( NAME, class_file, refresh_rate)
     */

    @Override
    public void initThreads() {
        new ConstructThread("thread0", new Thread_0(), 2);
        new ConstructThread("thread1", new Thread_1(), 2);
    }
}
