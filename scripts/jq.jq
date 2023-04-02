def fillPlayerData(row; playerIndex):
if(row[(playerIndex | tostring) + "-Gracz"] == null) then empty else
{
	playerName: row[(playerIndex | tostring) + "-Gracz"],
	playerScore: row["Punkty-"+(playerIndex | tostring)],
	isWinner: ( row["Win-"+(playerIndex | tostring)]
				| if(. == "TRUE") then true else null end ),
	comment: row["Komentarz-"+(playerIndex | tostring)]
} end;
map(
	. as $row
	| {
		timestamp: .ID,
		gameName: .Gra,
		gameDescription: ."Opis gry",
		gameDate: .Data,
		solo: .Solo,
		duration: .Czas,
		playerResults:( [1,2,3,4,5,6,7] | map(fillPlayerData($row; .) ))
	  }
	| del(..|nulls) 
)
