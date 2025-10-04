import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import NavBar from './components/user/navbar/NavBar';
import "./index.css";
import App from "./App";



ReactDOM.render(
	<React.StrictMode>
		<Router>
			{/* <NavBar/> */}
			<Routes>
				<Route path="/" element={<App />}></Route>
			</Routes>
			<App />
		</Router>
	</React.StrictMode>,
	document.getElementById("root")
);
