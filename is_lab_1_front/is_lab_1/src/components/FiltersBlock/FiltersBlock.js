import React from 'react';
import './FiltersBlock.css';

function FiltersBlock({ filters }) {
    const {
        nameFilter, setNameFilter,
        ageFilter, setAgeFilter,
        typeFilter, setTypeFilter,
        locationFilter, setLocationFilter,
        populationFilter, setPopulationFilter,
        populationDensityFilter, setPopulationDensityFilter,
        attackLevelFilter, setAttackLevelFilter,
        defenseLevelFilter, setDefenseLevelFilter,
        ringPowerFilter, setRingPowerFilter,
        ringWeightFilter, setRingWeightFilter,

    } = filters;

    return (
        <div className="filters-and-actions">
            <div className="filter-container">
                <input
                    type="text"
                    placeholder="Имя"
                    value={nameFilter}
                    onChange={(e) => setNameFilter(e.target.value)}
                />
            </div>
            <div className="filter-container">
                <input
                    type="number"
                    placeholder="Возраст"
                    value={ageFilter}
                    onChange={(e) => setAgeFilter(e.target.value)}
                />
            </div>
            <div className="filter-container">
                <input
                    type="text"
                    placeholder="Тип"
                    value={typeFilter}
                    onChange={(e) => setTypeFilter(e.target.value)}
                />
            </div>
            <div className="filter-container">
                <input
                    type="text"
                    placeholder="Название города"
                    value={locationFilter}
                    onChange={(e) => setLocationFilter(e.target.value)}
                />
            </div>
            <div className="filter-container">
                <input
                    type="text"
                    placeholder="Население"
                    value={populationFilter}
                    onChange={(e) => setPopulationFilter(e.target.value)}
                />
            </div>
            <div className="filter-container">
                <input
                    type="text"
                    placeholder="Плотность населения"
                    value={populationDensityFilter}
                    onChange={(e) => setPopulationDensityFilter(e.target.value)}
                />
            </div>
            <div className="filter-container">
                <input
                    type="number"
                    placeholder="Уровень атаки"
                    value={attackLevelFilter}
                    onChange={(e) => setAttackLevelFilter(e.target.value)}
                />
            </div>
            <div className="filter-container">
                <input
                    type="number"
                    placeholder="Уровень защиты"
                    value={defenseLevelFilter}
                    onChange={(e) => setDefenseLevelFilter(e.target.value)}
                />
            </div>
            <div className="filter-container">
                <input
                    type="text"
                    placeholder="Сила кольца"
                    value={ringPowerFilter}
                    onChange={(e) => setRingPowerFilter(e.target.value)}
                />
            </div>
            <div className="filter-container">
                <input
                    type="text"
                    placeholder="Вес кольца"
                    value={ringWeightFilter}
                    onChange={(e) => setRingWeightFilter(e.target.value)}
                />
            </div>
        </div>
    );
}

export default FiltersBlock;
