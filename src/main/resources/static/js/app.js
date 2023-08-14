document.addEventListener('DOMContentLoaded', function () {
    const applicationForm = document.getElementById('applicationForm');
    const loader = document.getElementById('loader');
    const offersDiv = document.getElementById('offers');
    var baseUrl = /*[[${baseUrl}]]*/ '';

    applicationForm.addEventListener('submit', function (event) {
        event.preventDefault();

        loader.style.display = 'block';

        const agreeToDataUsageCheckbox = document.getElementById('agreeToDataUsage');
        if (agreeToDataUsageCheckbox.checked) {
            agreeToDataUsageCheckbox.value = 'true';
        } else {
            agreeToDataUsageCheckbox.value = 'false';
        }

        const formData = new FormData(applicationForm);

        fetch(baseUrl+'/api/apply', {
            method: 'POST',
            body: JSON.stringify(Object.fromEntries(formData)),
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => response.json())
            .then(data => {
                loader.style.display = 'none';
                offersDiv.style.display = 'block';
                const offers = data.offers.map(offer => `
                <p>Source: ${offer.source}</p>
                <p>Monthly Payment Amount: ${offer.monthlyPaymentAmount}</p>
                <p>Total Repayment Amount: ${offer.totalRepaymentAmount}</p>
                <p>Number of Payments: ${offer.numberOfPayments}</p>
                <p>Annual Percentage Rate: ${offer.annualPercentageRate}</p>
                <p>First Repayment Date: ${offer.firstRepaymentDate}</p>
                <hr>
            `).join('');
                offersDiv.innerHTML = offers;
            })
            .catch(error => {
                console.error('Error:', error);
                loader.style.display = 'none';
            });
    });
});