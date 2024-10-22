import React, { useState } from 'react';
import Button from "./Button/Button"; // Ensure this path is correct
import './SpecialActions.css';

const API_BASE_URL = 'http://localhost:8080/api/book-creatures';

function SpecialActions({ token }) {
    const [minWeightCreature, setMinWeightCreature] = useState(null);
    const [ringName, setRingName] = useState(''); // State for ring name input
    const [creatureCount, setCreatureCount] = useState(null); // State for creature count
    const [creatureSearchResults, setCreatureSearchResults] = useState([]); // State for creatures found by name substring
    const [nameSubstring, setNameSubstring] = useState(''); // State for the name substring input
    const [error, setError] = useState(null);
    const [creatureId1, setCreatureId1] = useState(''); // State for the first creature ID
    const [creatureId2, setCreatureId2] = useState(''); // State for the second creature ID
    const [strongestRingCreature, setStrongestRingCreature] = useState(null);

    // Function to fetch the creature with minimum ring weight
    const getBookCreatureWithMinRingWeight = async (token) => {
        const response = await fetch(`${API_BASE_URL}/min-ring-weight`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json',
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error fetching book creature with minimum ring weight: ${response.status}`);
        }

        const data = await response.json();
        return data;
    };

    const handleGetMinRingWeightCreature = async () => {
        try {
            const creature = await getBookCreatureWithMinRingWeight(token);
            setMinWeightCreature(creature);
            setError(null);
        } catch (err) {
            setError(err.message);
            setMinWeightCreature(null);
        }
    };

    // Function to get the count of creatures by ring name
    const getCreatureCountByRing = async (ringName, token) => {
        const response = await fetch(`${API_BASE_URL}/count-by-ring?ringName=${encodeURIComponent(ringName)}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json',
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error fetching creature count for ring: ${ringName}`);
        }

        const count = await response.json();
        return count;
    };

    const handleGetCreatureCountByRing = async () => {
        try {
            const count = await getCreatureCountByRing(ringName, token);
            setCreatureCount(count);
            setError(null);
        } catch (err) {
            setError(err.message);
            setCreatureCount(null);
        }
    };

    // Function to search creatures by name substring
    const searchCreaturesByNameSubstring = async (nameSubstring, token) => {
        const response = await fetch(`${API_BASE_URL}/search-by-name?substring=${encodeURIComponent(nameSubstring)}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json',
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error fetching creatures by name substring: ${response.status}`);
        }

        const creatures = await response.json();
        return creatures;
    };

    const handleSearchCreaturesByName = async () => {
        try {
            const creatures = await searchCreaturesByNameSubstring(nameSubstring, token);
            setCreatureSearchResults(creatures);
            setError(null);
        } catch (err) {
            setError(err.message);
            setCreatureSearchResults([]);
        }
    };

    // Function to exchange rings between two creatures
    const exchangeRings = async () => {
        const response = await fetch(`${API_BASE_URL}/exchange-rings?id1=${creatureId1}&id2=${creatureId2}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json',
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error exchanging rings: ${errorText}`);
        }

        // Reset creature IDs after successful exchange
        setCreatureId1('');
        setCreatureId2('');
    };

    const handleExchangeRings = async () => {
        try {
            await exchangeRings();
            setError(null);
            alert("Rings exchanged successfully!");
        } catch (err) {
            setError(err.message);
        }
    };

    const getBookCreatureWithStrongestRing = async (token) => {
        const response = await fetch(`${API_BASE_URL}/max-ring-weight`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json',
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error fetching book creature with strongest ring: ${response.status}`);
        }

        const data = await response.json();
        return data;
    };

    const handleGetStrongestRingCreature = async () => {
        try {
            const creature = await getBookCreatureWithStrongestRing(token);
            setStrongestRingCreature(creature);
            setError(null);
        } catch (err) {
            setError(err.message);
            setStrongestRingCreature(null);
        }
    };


    return (
        <div className="special-actions">
            <h3>Special Actions</h3>
            <Button onClick={handleGetMinRingWeightCreature} label="Get Creature with Min Ring Weight" />

            {minWeightCreature && (
                <div className="creature-info">
                    <h4>Creature with Minimum Ring Weight:</h4>
                    <p>Name: {minWeightCreature.name}</p>
                    <p>Ring Name: {minWeightCreature.ring.name}</p>
                    <p>Ring Weight: {minWeightCreature.ring.weight}</p>
                </div>
            )}

            <Button onClick={handleGetStrongestRingCreature} label="Get Creature with Strongest Ring" />
            {strongestRingCreature && (
                <div className="creature-info">
                    <h4>Creature with Strongest Ring:</h4>
                    <p>Name: {strongestRingCreature.name}</p>
                    <p>Ring Name: {strongestRingCreature.ring.name}</p>
                    <p>Ring Weight: {strongestRingCreature.ring.weight}</p>
                </div>
            )}

            <div className="creature-count-section">
                <h4>Count Creatures by Ring</h4>
                <input
                    type="text"
                    value={ringName}
                    onChange={(e) => setRingName(e.target.value)}
                    placeholder="Enter Ring Name"
                />
                <Button onClick={handleGetCreatureCountByRing} label="Get Creature Count" />

                {creatureCount !== null && (
                    <div className="creature-count-info">
                        <p>Number of creatures with ring "{ringName}": {creatureCount}</p>
                    </div>
                )}
            </div>

            <div className="creature-search-section">
                <h4>Search Creatures by Name Substring</h4>
                <input
                    type="text"
                    value={nameSubstring}
                    onChange={(e) => setNameSubstring(e.target.value)}
                    placeholder="Enter Name Substring"
                />
                <Button onClick={handleSearchCreaturesByName} label="Search Creatures" />

                {creatureSearchResults.length > 0 && (
                    <div className="creature-search-results">
                        <h4>Creatures Matching "{nameSubstring}":</h4>
                        <ul>
                            {creatureSearchResults.map(creature => (
                                <li key={creature.id}>
                                    <strong>Name:</strong> {creature.name} <br/>
                                    <strong>Age:</strong> {creature.age} <br/>
                                    <strong>Type:</strong> {creature.creatureType} <br/>
                                    <strong>Location:</strong> {creature.creatureLocation?.name} <br/>
                                    <strong>Coordinates:</strong> {creature.coordinates?.latitude}, {creature.coordinates?.longitude}
                                    <br/>
                                    <strong>Attack Level:</strong> {creature.attackLevel} <br/>
                                    <strong>Defense Level:</strong> {creature.defenseLevel} <br/>
                                    <strong>Creation Date:</strong> {new Date(creature.creationDate).toLocaleString()}
                                    <br/>
                                    <strong>Ring:</strong> {creature.ring?.name} <br/>
                                    <strong>Ring Weight:</strong> {creature.ring?.weight} <br/>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>

            <div className="ring-exchange-section">
                <h4>Exchange Rings Between Creatures</h4>
                <input
                    type="text"
                    value={creatureId1}
                    onChange={(e) => setCreatureId1(e.target.value)}
                    placeholder="Enter ID of Creature 1"
                />
                <input
                    type="text"
                    value={creatureId2}
                    onChange={(e) => setCreatureId2(e.target.value)}
                    placeholder="Enter ID of Creature 2"
                />
                <Button onClick={handleExchangeRings} label="Exchange Rings" />
            </div>

            {error && (
                <div className="error-message">
                    <p>{error}</p>
                </div>
            )}
        </div>
    );
}

export default SpecialActions;
