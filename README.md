Real Time Harmonic Accompaniment
================================

The written code here takes as an input a melody played by a user and outputs chordal accompaniment to fit the melody in real time. That is, as the user is playing a melody on a MIDI keyboard, harmonic accompaniment is automatically generated to fit the melody. The algorithm improves as the user continues playing, though it is still limited in the number of states available. Only the standard I to VII chords are considered, though the project is readily expandable to accomodate additional states.

Training
--------

MATLAB code was generated to create 200 melodies: each melody consists of anywhere between 200-400 notes. These melodies followed a few simple rules and so some of the results later achieved were expected, however, if the training data was instead a scraping of MIDI files, results could be improved. There were libraries utilized in MATLAB that faciliated conversion of MIDI to matrix array format. 

Following this, the data was read into the Java code using the JAHMM library. This generates a trained Hidden Markov Model. 

Generation
----------

The Hidden Markov Model, then, allows us to within some accuracy predict successive chords based on previous chords, as well as the note in the melody that is currently being played. By navigating through the available states, then matching against state transition variables, such transitions were decided.

