nextRow([_], []).
nextRow([H, M|T], [A|B]) :-
    nextRow([M|T], B), 
    A is M - H.

myLast([X], X).
myLast([_|T], R) :-
    myLast(T, R).

nextItem([O], O).
nextItem(L, N) :-
    nextRow(L, P),
    myLast(L, Q),
    nextItem(P, W),
    N is Q+W.