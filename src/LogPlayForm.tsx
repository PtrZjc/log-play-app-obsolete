import React, {useState} from "react";
import {Button, Card, Checkbox, DatePicker, Select} from "antd";
import Input from "antd/lib/input/Input";
import {GAME_NAMES, PLAYER_NAMES} from "./constants";
import moment from 'moment';

interface FormData {
    gameName: string,
    gameDescription: string | null,
    gameDate: string,
    solo: boolean,
    duration: string | null,
    playerResults: PlayersState,
}

interface PlayerResult {
    playerName: string,
    playerScore: number | null,
    isWinner: boolean
    comment: string | null
}

const defaultPlayer: PlayerResult = {
    playerName: "",
    playerScore: null,
    isWinner: false,
    comment: null
}

interface PlayersState {
    [playerIndex: number]: PlayerResult
}

const initialPlayersState: PlayersState = [
    {...defaultPlayer, playerName: "ja"},
    {...defaultPlayer, playerName: "Ilona"},
    {...defaultPlayer},
    {...defaultPlayer},
    {...defaultPlayer},
]

export default function LogPlayForm() {

    const [gameName, setGameName] = useState(GAME_NAMES[0])
    const [gameDescription, setGameDescription] = useState<string | null>(null)
    const [playerState] = useState(initialPlayersState)
    const [playerQuantity, setPlayerQuantity] = useState(2)
    const [gameDate, setGameDate] = useState(moment(new Date()))
    const [duration, setDuration] = useState<string | null>(null)
    const [solo, setSolo] = useState(false)

    const {Option} = Select;

    function handleSubmit(event: React.ChangeEvent<any>) {
        event.preventDefault();
        console.log('gameName: ', gameName)
        console.log('gameDescription: ', gameDescription)
        console.log('playerQuantity: ', playerQuantity)
        console.log('playerState: ', playerState)
        console.log('duration: ', duration)
        console.log('solo: ', solo)

        let formData: FormData = {
            gameName: gameName,
            gameDescription: gameDescription,
            gameDate: gameDate.format("YYYY-MM-DD"),
            solo: solo,
            duration: duration,
            playerResults: playerState,
        }

        console.log(JSON.stringify(formData, null, 2))
    }

    return <Card
        bordered={true}
        style={{
            width: 600,
        }}
    >
        <Select
            showSearch
            defaultValue={GAME_NAMES[0]}
            onChange={(gameName) => setGameName(gameName)}
            style={{width: 400}}
        >
            {GAME_NAMES.map((game) => <Option value={game}>{game}</Option>)}
        </Select>

        <Checkbox
            defaultChecked={solo}
            onChange={() => setSolo(!solo)}
        >Solo game</Checkbox>

        <Input
            id="gameDescription"
            placeholder="game description"
            onChange={e => setGameDescription(e.target.value)}
        />

        <DatePicker
            defaultPickerValue={gameDate}
            defaultValue={moment(new Date())}
            onChange={(date) => setGameDate(date!!)} />

        <Input type="time"
               placeholder={"game duration"}
               onChange={(e) => setDuration(e.target.value)} />

        <Select
            defaultValue={playerQuantity}
            onChange={(quantity) => setPlayerQuantity(quantity)}>
            {[1, 2, 3, 4, 5].map((number) => <Option value={number}>{number}</Option>)}
        </Select>

        {[...Array(playerQuantity).keys()].map(playerIndex =>
            <Card
                title={`Player  ${playerIndex + 1} name`}
                bordered={true}
                style={{
                    width: 500,
                }}
            >
                <Select
                    showSearch
                    defaultValue={playerState[playerIndex]?.playerName}
                    onChange={playerName => playerState[playerIndex].playerName = playerName}
                >
                    {PLAYER_NAMES.map((name) => <Option value={name}>{name}</Option>)}
                </Select>
                <Input
                    type={"number"}
                    placeholder="player score"
                    onChange={(e) => playerState[playerIndex].playerScore = parseInt(e.target.value)}
                />
                <Checkbox
                    onChange={() => playerState[playerIndex].isWinner = !playerState[playerIndex].isWinner}
                />
                <Input
                    placeholder="comments"
                    onChange={(e) => playerState[playerIndex].comment = e.target.value}
                />
            </Card>
        )}
        <Button onClick={handleSubmit}>Send</Button>
    </Card>
}
