const axios = require('axios')

const host = "http://localhost:8100"
const participantEmailOne = "mitchell.herrijgers@axoniq.io"
const participantEmailTwo = "steven@axoniq.io"

const objectName = "Awesome Teddybear"

const auctionTime = 5
const minimumBid = 2
const actualBid = 3

async function run() {
    const participantOneId = (await (axios.post(`${host}/participants`, {email: participantEmailOne}))).data;
    const participantTwoId = (await (axios.post(`${host}/participants`, {email: participantEmailTwo}))).data;
    const objectId = (await (axios.post(`${host}/objects`, {owner: participantOneId, name: objectName}))).data;
    const auctionId = (await (axios.post(`${host}/auctions`, {objectId: objectId, minimumBid, duration: auctionTime}))).data;

    console.log("Active auctions:")
    console.log((await axios.get(`${host}/auctions/active`)).data)

    console.log("Auctions for Participant one:")
    console.log((await axios.get(`${host}/participants/${participantOneId}/auctions`)).data)

    console.log("Bids for participant two:")
    console.log((await axios.get(`${host}/participants/${participantTwoId}/bids`)).data)

    await axios.post(`${host}/auctions/${auctionId}/bid`, {participant: participantTwoId, bid: actualBid})

    console.log("Bids for participant two:")
    console.log((await axios.get(`${host}/participants/${participantTwoId}/bids`)).data)


    console.log("Will wait for end of auction to fetch auction state....")
    setTimeout(async () => {
        console.log((await axios.get(`${host}/auctions/${auctionId}`)).data)
    }, auctionTime * 1000 + 500)
}

run()
