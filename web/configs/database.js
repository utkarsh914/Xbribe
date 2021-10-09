const mongoose = require('mongoose');
// mongoose doesn't use promises by default
mongoose.Promise = global.Promise;

const uristring =
	process.env.MONGODB_URI ||
	process.env.MONGOHQ_URL ||
	'mongodb://localhost:27017/bribery';

const InitiateMongoServer = async () => {
	try {
		await mongoose.connect(uristring, {
			useNewUrlParser: true,
			useUnifiedTopology: true,
			// useCreateIndex: true,
			// useFindAndModify: false,
		});
		console.log('Mongoose Connected!');
	}
	catch (err) {
		console.log('Mongoose Couldnt connect:', err);
	}
};


module.exports = InitiateMongoServer
