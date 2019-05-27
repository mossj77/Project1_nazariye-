import java.util.ArrayList;
import java.util.List;

public class MinimizeTable {
    MinimizeTable(List<State> enternalStates , State minimized , DFA_DS dfa ){
        InternalStates = enternalStates;
        Minimized = minimized;
        FillToDfaStates(this,dfa);
    }
    public List<State> InternalStates ;
    public State Minimized;
    public List<State[]> ToDfaStateByAlpha = new ArrayList<>();
    public List<State[]> ToMinimizedStateByAlpha = new ArrayList<>();

    public void FillToDfaStates(MinimizeTable minimizeTable , DFA_DS dfa) {
        for (State s:minimizeTable.InternalStates) {
            State[] stateArray = new State[dfa.Alphabet.length];
            for (int i = 0; i < dfa.Alphabet.length; i++) {
                for (Relation r:dfa.relations) {
                    if(r.FromState.equals(s) && r.ByAlphabet.equals(dfa.Alphabet[i])){
                        stateArray[i] = r.ToState;
                        break;
                    }
                }
            }
            minimizeTable.ToDfaStateByAlpha.add(stateArray);
        }
    }
}
