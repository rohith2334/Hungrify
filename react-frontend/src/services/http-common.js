import axios from 'axios';

export default axios.create({
    baseURL:'http://localhost:8080/api/food',
    headers:{
        'Content-Type':'application/json',
        'Cookie': 'bezkoder=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtb2QyMkBnbWFpbC5jb20iLCJpYXQiOjE3MDYzNjM0ODAsImV4cCI6MTcwNjQ0OTg4MH0.XPXcKjhT4bgw6ido5G3hz1baIUJuMGAeG9Td0FPOuPI'
    }
});