package com.mygdx.game.tools;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import static javax.sound.midi.MidiSystem.getMidiDevice;

public class MidiTools {

    public MidiTools() {
        try {
            Sequencer seq = MidiSystem.getSequencer();
            Synthesizer synth = MidiSystem.getSynthesizer();
            Instrument[] fish = synth.getAvailableInstruments();
            ShortMessage myMsg = new ShortMessage();
            myMsg.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
            long timeStamp = -1;
            MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
            MidiDevice device =  MidiSystem.getMidiDevice(info[2]);
            device.open();
            Receiver rcvr = MidiSystem.getReceiver();//device.getReceiver();
            rcvr.send(myMsg, timeStamp);
            device.close();
            int goodNews = 0;
            goodNews++;
        } catch (Exception e) {
            int badNews = 0;
            badNews++;
        }

    }
}
