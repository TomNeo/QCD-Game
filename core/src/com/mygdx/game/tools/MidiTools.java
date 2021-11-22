package com.mygdx.game.tools;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import static javax.sound.midi.MidiSystem.getMidiDevice;

public class MidiTools {

    Sequencer seq; //= MidiSystem.getSequencer();
    Synthesizer synth; //= MidiSystem.getSynthesizer();
    Boolean notNull;

    public MidiTools() {
        try {
            seq = MidiSystem.getSequencer();
            synth = MidiSystem.getSynthesizer();
            synth.open();
            notNull = true;
        } catch (Exception e) {
            notNull = false;
        }
    }

    public Instrument changeInsturment(int instrument){
        Soundbank soundbank = synth.getDefaultSoundbank();
        Instrument[] instr = soundbank.getInstruments();
        synth.loadInstrument(instr[instrument]);    //Changing this int (instrument) does nothing
        return instr[instrument];
    }

    public void startMidiChannel(int channel, int note, int force, Instrument instru){
        MidiChannel[] mc = synth.getChannels();
        mc[channel].programChange(instru.getPatch().getProgram());
        mc[channel].noteOn(note, force);
    }

    public void stopMidiChannel(int channel, int note, int force){
        MidiChannel[] mc = synth.getChannels();
        mc[channel].noteOff(note, force);
    }


    public void dispose(){
        synth.close();
    }
}
