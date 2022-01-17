const fs = require("fs");
const LoremIpsum = require("lorem-ipsum").LoremIpsum;

// Template values
const typeList = ["apartment", "serviced", "street"];
const statusList = ["available", "reserved", "rented"];

const locationList = [
  {
    city: "Saigon",
    district: 1,
  },
  {
    city: "Saigon",
    district: 4,
  },
  {
    city: "Saigon",
    district: 7,
  },
  {
    city: "Saigon",
    district: 10,
  },
  {
    city: "Hanoi",
    district: "Dong Da",
  },
  {
    city: "Hanoi",
    district: "Ba Dinh",
  },
  {
    city: "Hanoi",
    district: "Cau Giay",
  },
];

const lorem = new LoremIpsum({
  sentencesPerParagraph: {
    max: 2,
    min: 1,
  },
  wordsPerSentence: {
    max: 12,
    min: 4,
  },
});

// Generate random float number
function getRandomInRange(from, to, fixed) {
  return (Math.random() * (to - from) + from).toFixed(fixed) * 1;
  // .toFixed() returns string, so ' * 1' is a trick to convert to number
}

let fileNo = 1;
let n = 1;
let len = 100;

// Generate n files, each having {len} records
for (fileNo; fileNo <= 1; fileNo++) {
  // Reset array for each file
  let data = [];

  // Generate 100000 rows per file
  for (n; n <= len; n++) {
    let typeRand = Math.floor(Math.random() * typeList.length);
    let statusRand = Math.floor(Math.random() * statusList.length);
    let locationRand = Math.floor(Math.random() * locationList.length);

    let imageIndex = Math.floor(Math.random() * 500 + 1);

    let house = {
      houseId: n,
      name: lorem.generateSentences(1),
      price: Math.floor(Math.random() * (1000 - 200 + 1)) + 200,
      // Math.floor(Math.random() * (maximum - minimum + 1)) + minimum;
      description: lorem.generateParagraphs(1),
      address: lorem.generateSentences(1),
      longitude: getRandomInRange(-180, 180, 6),
      latitude: getRandomInRange(-90, 90, 6),
      image: [
        `https://realestatemgt.s3.ap-southeast-1.amazonaws.com/dataset/${imageIndex}/${imageIndex}_bathroom.jpg`,
        `https://realestatemgt.s3.ap-southeast-1.amazonaws.com/dataset/${imageIndex}/${imageIndex}_kitchen.jpg`,
        `https://realestatemgt.s3.ap-southeast-1.amazonaws.com/dataset/${imageIndex}/${imageIndex}_bedroom.jpg`,
        `https://realestatemgt.s3.ap-southeast-1.amazonaws.com/dataset/${imageIndex}/${imageIndex}_frontal.jpg`,
      ],
      type: typeList[typeRand],
      numberOfBeds: Math.floor(Math.random() * 5 + 1),
      squareFeet: Math.floor(Math.random() * 200 + 100),
      status: statusList[statusRand],
      location: locationList[locationRand],
    };

    data.push(house);
  }

  // Write to JSON file
  const jsonContent = JSON.stringify(data);
  fs.writeFile(`./house_${fileNo}.json`, jsonContent, "utf8", function (err) {
    if (err) {
      return console.log(err);
    }

    console.log("File is saved!");
  });

  // Update len
  len += len;
}
