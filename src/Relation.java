public class Relation {
    Relation(State fromState,String byalphabet,State toState){
        FromState = fromState;
        ByAlphabet = byalphabet;
        ToState = toState;
    }
    public State FromState;
    public State ToState;
    public String ByAlphabet;
}
