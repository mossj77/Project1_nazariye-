import java.util.List;

public class MatchState {
    MatchState(State dfaState, List<State> matchedNfaState){
        DfaState = dfaState;
        MatchedNfaState = matchedNfaState;
    }
    State DfaState;
    List<State> MatchedNfaState;
}
