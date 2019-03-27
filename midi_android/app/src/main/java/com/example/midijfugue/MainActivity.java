package com.example.midijfugue;

import android.content.pm.PackageManager;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.temporal.TemporalEvents;

import java.io.IOException;
import java.util.ArrayList;

import jp.kshoji.javax.sound.midi.MidiEvent;
import jp.kshoji.javax.sound.midi.MidiMessage;
import jp.kshoji.javax.sound.midi.Sequence;
import jp.kshoji.javax.sound.midi.Track;
import jp.kshoji.javax.swing.event.EventListenerList;

public class MainActivity extends AppCompatActivity {
    public byte[] buffer = new byte[32];
    public int numBytes = 0;
    public MidiInputPort inputPort;
    public int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void playNote(View view) {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            setupMidi();
        }
    }

    private void playCNote() throws IOException {
        Player player = new Player();
        Sequence s = player.getSequence("C");
        for (int i = 0; i < s.getTracks().length; i++) {
            Track track = s.getTracks()[i];
            for (int j = 0; j < track.size(); j++) {
                MidiEvent event = track.get(j);
                MidiMessage message = event.getMessage();
                byte[] m = message.getMessage();
                Log.e("event", "" + event);
                Log.e("message", "" + message);
                initialiseBuffer(m);
                inputPort.send(buffer,offset, numBytes);
//                sendNoteOn();
            }

//            for(MidiEvent event:s.getTracks()[i]){
//                Log.e("event", ""+event);
            Log.e("track", "" + track);
        }

    }
//        s.getTracks();
    //        Toast.makeText(this, " " + player.getSequence(pattern),Toast.LENGTH_LONG).show();

//        Log.e("sequence", player.getSequence(pattern).toString());


    private void setupMidi() {
        MidiManager m = (MidiManager) this.getSystemService(MIDI_SERVICE);
        MidiDeviceInfo[] infos = m.getDevices();
//        int numDevices = infos.length;

        for (MidiDeviceInfo i : infos) {
            System.out.println(i);
        }
        final MidiDeviceInfo info = infos[0];
        int numInputs = info.getInputPortCount();
        int numOutputs = info.getOutputPortCount();

        Log.e("InputPort", "numInputs: " + numInputs);
        Log.e("OutputPort", "numOuputs " + numOutputs);

        m.openDevice(info, new MidiManager.OnDeviceOpenedListener() {
                    @Override
                    public void onDeviceOpened(MidiDevice device) {
                        if (device == null) {
                            Log.e("null", "could not open device " + info);
                        } else {
                            Log.e("connected to", "device: " + info);
                            MidiInputPort inputPort = device.openInputPort(0);
                            try {
                                playCNote();
//                                sendNoteOn();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                                sendNoteOn(inputPort);
                        }
                    }
                }, new Handler(Looper.getMainLooper())
        );


    }

    private void sendNoteOn() throws IOException {
        int channel = 3; // MIDI channels 1-16 are encoded as 0-15.
        buffer[numBytes++] = (byte)(0x90 + (channel - 1)); // note on
        buffer[numBytes++] = (byte)60; // pitch is middle C
        buffer[numBytes++] = (byte)127; // max velocity
        int offset = 0;
        // post is non-blocking
        inputPort.send(buffer, offset, numBytes);
    }

    private void initialiseBuffer(byte[] message) throws IOException {
        int channel = 3;
        buffer[numBytes++] = (byte) (message[0] + (channel - 1));
        buffer[numBytes++] = message[1];
        buffer[numBytes++] = message[2];

        Log.e("buffer", ""+ buffer);

    }

}
