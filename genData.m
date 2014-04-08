chords = 1:7;

transitionMatrix = [0, .069, 0, .289, .495, .138, .01;
    .208, 0, .098, .220, .232, .232, .01;
    .068, .113, 0, .371, .090, .349, .01;
    .527, 0, 0, 0, .347, .116, .01;
    .334, .074, 0, .285, 0, .297, .01;
    .140, .076, .076, .381, .317, 0, .01;
    .333, .094, 0, 0, .104, .469, 0];

chordNotes = [1, 3, 5;
    2, 4, 6;
    3, 5, 7;
    1, 4, 6;
    2, 5, 7;
    1, 3, 6;
    2, 4, 7];

numChords = 100;
notesToSample = [2, 4, 8, 16];

for row = (1:300)
    chordList = ones(1, numChords);
    
    % Construct the list of chords from the transition matrix
    for ii = 2:numChords
        previousChord = chordList(ii - 1);
        scalars = rand(1, 7) ./ 10;
        scaledTransition = transitionMatrix(previousChord,:).*scalars;
        nextChord = find(scaledTransition == max(scaledTransition));    
        if length(nextChord) ~= 1
            nextChord = nextChord(1);
        end
        chordList(ii) = nextChord;
    end
    
    index = 1;
    noteList = [];
    
    for ii = 1:(numChords - 1) 
        numberNotes = datasample(notesToSample, 1); % Randomly choose how many notes for this chord
        addedNotes = zeros(1, numberNotes);
        chordNumber = chordList(ii);
        chordalTones = chordNotes(chordNumber, :);
        
        for jj = 1:numberNotes
            addedNotes(jj) = datasample(chordalTones, 1);
%             if numberNotes == 2 || numberNotes == 4
%                 addedNotes(jj) = datasample(chordalTones, 1);
%             elseif numberNotes == 8
%                 if mod(jj, 2) ~= 0
%                     addedNotes(jj) = datasample(chordalTones, 1);
%                 else
%                     addedNotes(jj) = datasample((1:7), 1);
%                 end
%             else
%                 if mod(jj, 4) == 1
%                     addedNotes(jj) = datasample(chordalTones, 1);
%                 else
%                     addedNotes(jj) = datasample((1:7), 1);
%                 end
%             end
        end
        
        noteList = horzcat(noteList, addedNotes);
    end
    
    melody = (noteList') - 1;

    if (row == 1)
        fileID = fopen('trainingData.txt','w+');
    else
        fileID = fopen('trainingData.txt','a');
    end
    
    for abc=(1:length(melody))
        fprintf(fileID, '%d % 5.2f', melody(abc));
    end
    
    if (row ~= 10)
        fprintf(fileID,'\n');
    end
    
    fclose(fileID);
end